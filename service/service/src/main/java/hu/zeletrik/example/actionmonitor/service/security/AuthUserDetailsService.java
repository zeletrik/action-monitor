package hu.zeletrik.example.actionmonitor.service.security;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.zeletrik.example.actionmonitor.data.entity.RoleEntity;
import hu.zeletrik.example.actionmonitor.data.entity.UserEntity;
import hu.zeletrik.example.actionmonitor.data.repository.UserRepository;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;
import hu.zeletrik.example.actionmonitor.service.security.exception.NoUserFoundException;

/**
 * Custom implementation of {@link UserDetailsService} to handle auth flow.
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserDetailsService.class);

    private final UserRepository userRepository;
    private final ConversionService conversionService;

    public AuthUserDetailsService(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails result = null;
        try {
            var entity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new NoUserFoundException("No user found in the database"));
            var user = conversionService.convert(entity, UserDTO.class);
            result = new CustomUserDetails(user, getAuthorities(entity));
        } catch (Exception ex) {
            LOGGER.error("Exception in CustomUserDetailsService: " + ex);
        }
        return result;
    }

    private Collection<GrantedAuthority> getAuthorities(UserEntity user) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        for (RoleEntity role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
