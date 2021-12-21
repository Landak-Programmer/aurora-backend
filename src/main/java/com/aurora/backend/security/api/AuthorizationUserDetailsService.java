package com.aurora.backend.security.api;

import com.aurora.backend.exception.UnauthorizedUserException;
import com.aurora.backend.security.UserAware;
import com.aurora.backend.security.UserPrincipal;
import com.aurora.backend.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

import static com.aurora.backend.security.api.AuthorityHelper.getAuthorities;

@Service
public class AuthorizationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationUserDetailsService.class);

    @Autowired
    private LoginService loginService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        UserAware authenticatedUser = null;
        final String authorization = token.getCredentials().toString();
        if (authorization == null) return null;

        try {
            if (token.getCredentials() instanceof String) {
                authenticatedUser = loginService.login(authorization);
            } else {
                String[] credentials = (String[]) token.getCredentials();
                authenticatedUser = loginService.login(credentials[0], credentials[1]);
            }
        } catch (UnauthorizedUserException | NoSuchAlgorithmException e) {
            // do nothing for now
            LOGGER.warn("User are unauthorized. Msg {}", e.getMessage(), e);
        }

        if (authenticatedUser == null) {
            throw new BadCredentialsException("Invalid credential");
        }

        return new UserPrincipal(authenticatedUser, getAuthorities(authenticatedUser));
    }

}
