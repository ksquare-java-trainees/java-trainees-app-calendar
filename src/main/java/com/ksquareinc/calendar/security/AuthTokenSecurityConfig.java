package com.ksquareinc.calendar.security;

import com.ksquareinc.calendar.controller.SsoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:sec.properties")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AuthTokenSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    Environment environment;

    public static String ssoApiURI;
    public static String tokenKey;

    private final String INVALID_TOKEN_MSG = "Your token is not valid or has expired. Please try again.";


    private String[] SEC_WHITELIST = new String[]{"/", "/favicon.ico", "/assets/**", "/images/**",
            "/csrf", "/v2/api-docs", "/swagger-resources/configuration/ui",
            "/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security",
            "/configuration/security", "/swagger-ui.html", "/webjars/**"};


    @Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
        webSecurity.ignoring().antMatchers(SEC_WHITELIST);
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        tokenKey = environment.getProperty("tokenName");
        ssoApiURI = environment.getProperty("ssoAPIUri");

        PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter(tokenKey);
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            if(!SsoController.isTokenValid(principal)){
                throw new BadCredentialsException(INVALID_TOKEN_MSG);
            }
            authentication.setAuthenticated(true);
            return authentication;
        });


        httpSecurity.authorizeRequests().antMatchers(SEC_WHITELIST).permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/**")
                .authenticated()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(filter)
                .addFilterBefore(new ExceptionTranslationFilter(new Http403ForbiddenEntryPoint()),
                        filter.getClass()
                );




    }


}
