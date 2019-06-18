package com.ksquareinc.calendar.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    //No code needed. The objective is avoid the code in web.xml based configuration
    //about DelegatingFilterProxy
}
