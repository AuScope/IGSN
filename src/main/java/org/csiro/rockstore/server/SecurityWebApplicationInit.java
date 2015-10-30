package org.csiro.rockstore.server;

import org.csiro.rockstore.security.SecurityConfig;
import org.csiro.rockstore.security.UserDetailsContextMapperImpl;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInit
      extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInit() {
        super(SecurityConfig.class);
    }
}