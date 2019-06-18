package com.ksquareinc.calendar.security;

import com.ksquareinc.calendar.controller.SsoController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:sec.properties")
public class AuthTokenSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${tokenName}")
    public static String authHeaderName = "SSO_TOKEN";
    @Value("${authUri}")
    private String authURI;
    @Value("${ssoAPIUri}")
    public static String ssoApuURI = "http://localhost:8066/ksquare-sso/";
    private String INVALID_TOKEN_MSG = "Your token is not valid or has expired. Please try again.";


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        SsoController.BASE_URL = ssoApuURI;

        PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter(authHeaderName);
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            //if(!authHeaderValue.equals(principal)){
            if(!SsoController.isTokenValid(principal)){
                throw new BadCredentialsException(INVALID_TOKEN_MSG);
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity.antMatcher("/event/**")
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .addFilterBefore(new ExceptionTranslationFilter(new Http403ForbiddenEntryPoint()),
                        filter.getClass()
                )
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }


}
