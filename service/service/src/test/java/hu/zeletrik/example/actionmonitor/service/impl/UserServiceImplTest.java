package hu.zeletrik.example.actionmonitor.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.data.entity.UserEntity;
import hu.zeletrik.example.actionmonitor.data.repository.UserRepository;
import hu.zeletrik.example.actionmonitor.service.dto.ServiceResponse;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;

/**
 * Unit tests for {@link UserServiceImpl}.
 */
public class UserServiceImplTest {

    private static final String USERNAME = "TEST";
    private UserEntity entity;

    @Mock
    private ConversionService conversionServiceMock;

    @Mock
    private UserRepository userRepositoryMock;

    private UserServiceImpl underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new UserServiceImpl(userRepositoryMock, conversionServiceMock);
        entity = new UserEntity();
    }

    @Test
    public void findByUsernameShouldReturnSuccess() {
        //given
        entity.setUsername(USERNAME);
        var DTO = UserDTO.builder().username(USERNAME).build();

        var expected = ServiceResponse.<UserDTO>builder()
                .success(true)
                .body(DTO)
                .build();

        given(userRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.of(entity));
        given(conversionServiceMock.convert(entity, UserDTO.class)).willReturn(DTO);

        //when
        var actual = underTest.findByUsername(USERNAME);

        //then
        assertThat(actual, is(expected));
        verify(userRepositoryMock).findByUsername(USERNAME);
        verify(conversionServiceMock).convert(entity, UserDTO.class);
        verifyNoMoreInteractions(userRepositoryMock, conversionServiceMock);
    }

    @Test
    public void findByUsernameShouldReturnFailWhenDBError() {
        //given
        var DTO = UserDTO.builder().build();
        var expected = ServiceResponse.<UserDTO>builder()
                .success(false)
                .body(DTO)
                .build();

        given(userRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.empty());

        //when
        var actual = underTest.findByUsername(USERNAME);

        //then
        assertThat(actual, is(expected));
        verify(userRepositoryMock).findByUsername(USERNAME);
        verifyNoMoreInteractions(userRepositoryMock, conversionServiceMock);
    }

    @Test
    public void findByUsernameShouldReturnFailWhenConversionError() {
        //given
        entity.setUsername(USERNAME);
        var DTO = UserDTO.builder().build();

        var expected = ServiceResponse.<UserDTO>builder()
                .success(false)
                .body(DTO)
                .build();

        given(userRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.of(entity));
        given(conversionServiceMock.convert(entity, UserDTO.class)).willReturn(null);

        //when
        var actual = underTest.findByUsername(USERNAME);

        //then
        assertThat(actual, is(expected));
        verify(userRepositoryMock).findByUsername(USERNAME);
        verify(conversionServiceMock).convert(entity, UserDTO.class);
        verifyNoMoreInteractions(userRepositoryMock, conversionServiceMock);
    }

    @Test
    public void findByAlLShouldWork() {
        //given
        entity.setUsername(USERNAME);

        var DTO = UserDTO.builder().username(USERNAME).build();
        var expected = ServiceResponse.<List<UserDTO>>builder()
                .success(true)
                .body(List.of(DTO))
                .build();

        given(userRepositoryMock.findAll()).willReturn(List.of(entity));
        given(conversionServiceMock.convert(entity, UserDTO.class)).willReturn(DTO);

        //when
        var actual = underTest.findAll();

        //then
        assertThat(actual, is(expected));
        verify(userRepositoryMock).findAll();
        verify(conversionServiceMock).convert(entity, UserDTO.class);
        verifyNoMoreInteractions(userRepositoryMock, conversionServiceMock);
    }

    @Test
    public void findByAlLShouldWorkWhenEmptyList() {
        //given
        var DTO = UserDTO.builder().build();
        var expected = ServiceResponse.<List<UserDTO>>builder()
                .success(true)
                .body(Collections.emptyList())
                .build();

        given(userRepositoryMock.findAll()).willReturn(Collections.emptyList());

        //when
        var actual = underTest.findAll();

        //then
        assertThat(actual, is(expected));
        verify(userRepositoryMock).findAll();
        verifyNoMoreInteractions(userRepositoryMock, conversionServiceMock);
    }
}
