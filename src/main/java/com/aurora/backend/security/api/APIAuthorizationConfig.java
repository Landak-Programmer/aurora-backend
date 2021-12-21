package com.aurora.backend.security.api;

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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import java.util.Collections;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Handle API authorization via API key (token).
 *
 * @see AuthorizationUserDetailsService
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER - 9)
public class APIAuthorizationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CorsConfig corsConfig;
    @Autowired
    private AuthorizationUserDetailsService authorizationUserDetailsService;

    @Bean
    public RequestHeaderAuthenticationFilter preAuthorizationFilter() {
        RequestHeaderAuthenticationFilter preAuthenticationFilter = new RequestHeaderAuthenticationFilter();
        preAuthenticationFilter.setPrincipalRequestHeader("Authorization");
        preAuthenticationFilter.setCredentialsRequestHeader("Authorization");
        preAuthenticationFilter.setAuthenticationManager(authenticationManager());
        preAuthenticationFilter.setExceptionIfHeaderMissing(false);
        return preAuthenticationFilter;
    }

    @Bean
    public AuthenticationProvider authorizationProvider() {
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(authorizationUserDetailsService);
        authenticationProvider.setThrowExceptionWhenTokenRejected(false);
        return authenticationProvider;
    }

    @Override
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authorizationProvider()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .csrf().disable()
                .cors().configurationSource(corsConfig.corsConfigurationSource())
                .and()
                .addFilterAfter(preAuthorizationFilter(), RequestHeaderAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                .and() //FIXME: Fix this goddamn don't know
                .authorizeRequests()
                .antMatchers("/v2/pin/request", "/v2/pin/verify")
                .hasAnyRole("SUPER_ADMIN", "WEB", "MOBILE_ANDROID", "MOBILE_IOS")
                .antMatchers("/v2//admin/google/push",
                        "/v2/google-sign-in",
                        "/v2/app/requirement",
                        "/v2/smsapp/register",
                        "/v2/home-ownership-collection/submit",
                        "/v2/international-number-prefixes",
                        "/v2/properties/alerts",
                        "/v2/api-docs**").permitAll()
                .antMatchers().permitAll()
                .antMatchers("/v2/**").authenticated();
    }

}
