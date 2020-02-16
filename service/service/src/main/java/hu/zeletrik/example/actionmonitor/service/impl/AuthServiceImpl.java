package hu.zeletrik.example.actionmonitor.service.impl;

import hu.zeletrik.example.actionmonitor.service.AuthService;
import hu.zeletrik.example.actionmonitor.service.dto.ServiceResponse;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;
import hu.zeletrik.example.actionmonitor.service.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ServiceResponse<UserDTO> login(String username, String password) {
        var authenticated = false;
        UserDTO.UserDTOBuilder userBuilder = UserDTO.builder();
        var authenticationTokenRequest = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationTokenRequest);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            var user = (CustomUserDetails) authentication.getPrincipal();
            LOGGER.info("Logged in user: {}", user.getUser().getUsername());
            authenticated = true;
            userBuilder
                    .user_id(user.getUser().getUser_id())
                    .username(user.getUser().getUsername());

        } catch (BadCredentialsException ex) {
            LOGGER.info("Unsuccessful login, exception={}", ex.getMessage());
        }

        return ServiceResponse.<UserDTO>builder()
                .success(authenticated)
                .body(userBuilder.build())
                .build();
    }
}
