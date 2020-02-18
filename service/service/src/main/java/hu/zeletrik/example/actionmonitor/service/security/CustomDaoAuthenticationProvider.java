package hu.zeletrik.example.actionmonitor.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDaoAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final var name = authentication.getName();
        final var password = authentication.getCredentials().toString();
        UserDetails user = null;
        UsernamePasswordAuthenticationToken result = null;

        try {
            user = getUserDetailsService().loadUserByUsername(name);
        } catch (UsernameNotFoundException ex) {
            LOGGER.error("User '" + name + "' not found");
        } catch (Exception e) {
            LOGGER.error("Exception in CustomDaoAuthenticationProvider: " + e);
        }

        if (Objects.nonNull(user) && user.getPassword().equals(password)) {
            result = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException(messages.getMessage("CustomDaoAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        return result;
    }
}
