package com.ksquareinc.calendar.security;

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
    private String authHeaderName;

    @Value("${authUri}")
    private String authURI;
    //TODO: retrieve this token value from data source
    //@Value("${tokenVal}")
    //private String authHeaderValue;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter(authHeaderName);
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            //if(!authHeaderValue.equals(principal)){
            if(isActiveToken(principal)){
                throw new BadCredentialsException("Bad Token");
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

    private boolean isActiveToken(final String authToken) {
        ResponseEntity<String> response;
        RestTemplate rt = new RestTemplate();
        //final String authURI = "http://192.168.240.250:8888/ksquare-sso/api/users/auth";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try{
            response = rt.exchange(authURI, HttpMethod.GET, entity, String.class);
        }catch (HttpClientErrorException e) {
            response = ResponseEntity.status(403).body("Invalid Token");
        }

        return !response.getStatusCode().is2xxSuccessful();
    }
}
