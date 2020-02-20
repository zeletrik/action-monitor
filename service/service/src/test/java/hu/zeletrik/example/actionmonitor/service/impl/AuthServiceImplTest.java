package hu.zeletrik.example.actionmonitor.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Collections;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import hu.zeletrik.example.actionmonitor.service.dto.ServiceResponse;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;
import hu.zeletrik.example.actionmonitor.service.security.CustomUserDetails;

/**
 * Unit tests for {@link AuthServiceImplTest}.
 */
public class AuthServiceImplTest {

    private static final String USERNAME = "UNAME";
    private static final String PASSWORD = "PASS";

    @Mock
    private AuthenticationManager authenticationManagerMock;
    @Mock
    private Authentication authenticationMock;

    private AuthServiceImpl underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new AuthServiceImpl(authenticationManagerMock);
    }

    @Test
    public void loginShouldWork() {
        //given
        var authReq = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
        var dto = UserDTO.builder().user_id(1L).username(USERNAME).password(PASSWORD).build();
        var user = new CustomUserDetails(dto, Collections.emptyList());

        var expectedDTO = UserDTO.builder().user_id(1L).username(USERNAME).build();
        var expected = ServiceResponse.<UserDTO>builder()
                .success(true)
                .body(expectedDTO)
                .build();

        given(authenticationManagerMock.authenticate(authReq)).willReturn(authenticationMock);
        given(authenticationMock.getPrincipal()).willReturn(user);

        //when
        var actual = underTest.login(USERNAME, PASSWORD);

        //then
        assertThat(actual, is(expected));
        verify(authenticationManagerMock).authenticate(authReq);
        verify(authenticationMock).getPrincipal();
        verifyNoMoreInteractions(authenticationManagerMock, authenticationMock);

    }

    @Test
    public void loginShouldNotWork() {
        //given
        var authReq = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
        var dto = UserDTO.builder().user_id(1L).username(USERNAME).password(PASSWORD).build();
        var user = new CustomUserDetails(dto, Collections.emptyList());

        var expectedDTO = UserDTO.builder().build();
        var expected = ServiceResponse.<UserDTO>builder()
                .success(false)
                .body(expectedDTO)
                .build();

        given(authenticationManagerMock.authenticate(authReq)).willThrow(BadCredentialsException.class);

        //when
        var actual = underTest.login(USERNAME, PASSWORD);

        //then
        assertThat(actual, is(expected));
        verify(authenticationManagerMock).authenticate(authReq);
        verifyNoMoreInteractions(authenticationManagerMock, authenticationMock);

    }

}
