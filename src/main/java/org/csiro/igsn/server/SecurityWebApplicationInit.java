package org.csiro.igsn.server;

import org.csiro.igsn.security.SecurityConfig;
import org.csiro.igsn.security.UserDetailsContextMapperImpl;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInit
      extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInit() {
        super(SecurityConfig.class);
    }
}