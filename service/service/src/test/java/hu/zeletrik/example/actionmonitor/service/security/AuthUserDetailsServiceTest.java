package hu.zeletrik.example.actionmonitor.service.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.data.entity.RoleEntity;
import hu.zeletrik.example.actionmonitor.data.entity.UserEntity;
import hu.zeletrik.example.actionmonitor.data.repository.UserRepository;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;

/**
 * Unit tests for {@link AuthUserDetailsService}.
 */
public class AuthUserDetailsServiceTest {

    private static final String USERNAME = "UNAME";
    private static final String PASSWORD = "PASS";
    private static final String ROLE = "ROLE_TEST";
    private UserEntity entity;
    private RoleEntity role;

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private ConversionService conversionServiceMock;

    private AuthUserDetailsService underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new AuthUserDetailsService(userRepositoryMock, conversionServiceMock);

        role = new RoleEntity();
        role.setRole(ROLE);
        entity = new UserEntity();
        entity.setUsername(USERNAME);
        entity.setRoles(Set.of(role));
    }

    @Test
    public void loadUserByUsernameShouldWork() {
        //given
        var dto = UserDTO.builder().username(USERNAME).password(PASSWORD).build();
        var roles = List.of(new SimpleGrantedAuthority(ROLE));
        var expected = new CustomUserDetails(dto, roles);

        given(userRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.of(entity));
        given(conversionServiceMock.convert(entity, UserDTO.class)).willReturn(dto);

        //when
        var actual = underTest.loadUserByUsername(USERNAME);

        //then
        assertThat(actual, is(expected));
        verify(userRepositoryMock).findByUsername(USERNAME);
        verify(conversionServiceMock).convert(entity, UserDTO.class);
        verifyNoMoreInteractions(userRepositoryMock, conversionServiceMock);
    }

    @Test
    public void loadUserByUsernameShouldNotWorkWhenDbError() {
        //given
        given(userRepositoryMock.findByUsername(USERNAME)).willReturn(Optional.empty());

        //when
        var actual = underTest.loadUserByUsername(USERNAME);

        //then
        assertThat(actual, is(nullValue()));
        verify(userRepositoryMock).findByUsername(USERNAME);
        verifyNoMoreInteractions(userRepositoryMock, conversionServiceMock);
    }


}
