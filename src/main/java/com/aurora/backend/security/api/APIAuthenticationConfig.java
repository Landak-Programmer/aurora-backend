package com.aurora.backend.security.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
public class APIAuthenticationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CorsConfig corsConfig;
    @Autowired
    private AuthorizationUserDetailsService authorizationUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Bean
    public APIAuthenticationFilter preAuthenticationFilter() {
        APIAuthenticationFilter preAuthenticationFilter = new APIAuthenticationFilter();
        preAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return preAuthenticationFilter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(authorizationUserDetailsService);
        authenticationProvider.setThrowExceptionWhenTokenRejected(false);
        return authenticationProvider;
    }

    @Override
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .csrf().disable()
                .cors().configurationSource(corsConfig.corsConfigurationSource())
                .and()
                .antMatcher("/login")
                .addFilter(preAuthenticationFilter())
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers("/login")
                .authenticated();
/*                .antMatchers("/googleSignIn", "/v2/receiveGooglePush")
                .permitAll();*/
    }

    class APIAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

        @Override
        protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
            // Not being used
            return "basic";
        }

        @Override
        protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Credential credential = objectMapper.readValue(request.getInputStream(), Credential.class);
                String[] credentials = new String[]{credential.username, credential.password};
                return credentials;
            } catch (IOException e) {
                return null;
            }
        }

    }

}
