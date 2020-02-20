package hu.zeletrik.example.actionmonitor.web.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.security.Principal;
import java.util.List;

import hu.zeletrik.example.actionmonitor.web.rest.request.LoginRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.service.AuthService;
import hu.zeletrik.example.actionmonitor.service.UserService;
import hu.zeletrik.example.actionmonitor.service.dto.ServiceResponse;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;
import hu.zeletrik.example.actionmonitor.web.rest.response.UserResponse;

/**
 * Unit testes for {@link UserController}.
 */
public class UserControllerTest {

    private static final String USERNAME = "TEST";
    private static final String PASSWORD = "PASSWORD";
    private static final String USERNAME_2 = "TEST_2";

    @Mock
    private UserService userServiceMock;
    @Mock
    private AuthService authServiceMock;
    @Mock
    private ConversionService conversionServiceMock;
    @Mock
    private Principal principalMock;

    private UserController underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new UserController(userServiceMock, authServiceMock, conversionServiceMock);
    }

    @Test
    public void findAllOtherUserShouldWork() {
        //given
        var response = UserResponse.builder().username(USERNAME_2).build();

        var current = UserDTO.builder().username(USERNAME).build();
        var other = UserDTO.builder().username(USERNAME_2).build();
        var serviceResponse = ServiceResponse.<List<UserDTO>>builder()
                .success(true)
                .body(List.of(current, other))
                .build();

        given(userServiceMock.findAll()).willReturn(serviceResponse);
        given(principalMock.getName()).willReturn(USERNAME);
        given(conversionServiceMock.convert(other, UserResponse.class)).willReturn(response);

        //when
        var actual = underTest.findAllOtherUser(principalMock);

        //then
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        assertThat(actual.getBody(), is(List.of(response)));
        verify(userServiceMock).findAll();
        verify(conversionServiceMock).convert(other, UserResponse.class);
        verify(principalMock, times(2)).getName();
        verifyNoMoreInteractions(userServiceMock, principalMock, conversionServiceMock, authServiceMock);
    }


    @Test
    public void retrieveCurrentUserShouldWork() {
        //given
        var response = UserResponse.builder().username(USERNAME).build();

        var current = UserDTO.builder().username(USERNAME).build();
        var serviceResponse = ServiceResponse.<UserDTO>builder()
                .success(true)
                .body(current)
                .build();

        given(principalMock.getName()).willReturn(USERNAME);
        given(userServiceMock.findByUsername(USERNAME)).willReturn(serviceResponse);
        given(conversionServiceMock.convert(current, UserResponse.class)).willReturn(response);

        //when
        var actual = underTest.retrieveCurrentUser(principalMock);

        //then
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        assertThat(actual.getBody(), is(response));
        verify(principalMock).getName();
        verify(userServiceMock).findByUsername(USERNAME);
        verify(conversionServiceMock).convert(current, UserResponse.class);
        verifyNoMoreInteractions(userServiceMock, principalMock, conversionServiceMock, authServiceMock);
    }

    @Test
    public void retrieveCurrentUserShouldWorkWhenNoUser() {
        //given
        var response = UserResponse.builder().build();

        var current = UserDTO.builder().build();
        var serviceResponse = ServiceResponse.<UserDTO>builder()
                .success(false)
                .body(current)
                .build();

        given(principalMock.getName()).willReturn(USERNAME);
        given(userServiceMock.findByUsername(USERNAME)).willReturn(serviceResponse);
        given(conversionServiceMock.convert(current, UserResponse.class)).willReturn(response);

        //when
        var actual = underTest.retrieveCurrentUser(principalMock);

        //then
        assertThat(actual.getStatusCode(), is(HttpStatus.NO_CONTENT));
        assertThat(actual.getBody(), is(response));
        verify(principalMock).getName();
        verify(userServiceMock).findByUsername(USERNAME);
        verify(conversionServiceMock).convert(current, UserResponse.class);
        verifyNoMoreInteractions(userServiceMock, principalMock, conversionServiceMock, authServiceMock);
    }

    @Test
    public void loginShouldWork() {
        //given
        var request = new LoginRequest(USERNAME, PASSWORD);
        var response = UserResponse.builder().username(USERNAME).build();

        var current = UserDTO.builder().username(USERNAME).build();
        var serviceResponse = ServiceResponse.<UserDTO>builder()
                .success(true)
                .body(current)
                .build();

        given(principalMock.getName()).willReturn(USERNAME);
        given(authServiceMock.login(USERNAME, PASSWORD)).willReturn(serviceResponse);
        given(conversionServiceMock.convert(current, UserResponse.class)).willReturn(response);

        //when
        var actual = underTest.login(request);

        //then
        assertThat(actual.getStatusCode(), is(HttpStatus.OK));
        assertThat(actual.getBody(), is(response));
        verify(authServiceMock).login(USERNAME, PASSWORD);
        verify(conversionServiceMock).convert(current, UserResponse.class);
        verifyNoMoreInteractions(userServiceMock, principalMock, conversionServiceMock, authServiceMock);
    }

    @Test
    public void loginShouldWorkWhenBadCredentials() {
        //given
        var request = new LoginRequest(USERNAME, PASSWORD);
        var response = UserResponse.builder().build();

        var current = UserDTO.builder().build();
        var serviceResponse = ServiceResponse.<UserDTO>builder()
                .success(false)
                .body(current)
                .build();

        given(principalMock.getName()).willReturn(USERNAME);
        given(authServiceMock.login(USERNAME, PASSWORD)).willReturn(serviceResponse);
        given(conversionServiceMock.convert(current, UserResponse.class)).willReturn(response);

        //when
        var actual = underTest.login(request);

        //then
        assertThat(actual.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(actual.getBody(), is(response));
        verify(authServiceMock).login(USERNAME, PASSWORD);
        verify(conversionServiceMock).convert(current, UserResponse.class);
        verifyNoMoreInteractions(userServiceMock, principalMock, conversionServiceMock, authServiceMock);
    }
}
