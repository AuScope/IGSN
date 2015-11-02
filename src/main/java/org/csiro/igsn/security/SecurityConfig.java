package org.csiro.igsn.security;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.csiro.igsn.utilities.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.google.gson.Gson;


@Configuration
@EnableWebSecurity	
public  class SecurityConfig extends
		WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		http
        	.httpBasic()
        .and()
			.authorizeRequests()
				.antMatchers("/subnamespace/**").authenticated()		 
		 .and()
		    .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
			.csrf().csrfTokenRepository(csrfTokenRepository());
		
		
	}
	
	private CsrfTokenRepository csrfTokenRepository() {
		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		  repository.setHeaderName("X-XSRF-TOKEN");
		  return repository;
		}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	 
		auth.ldapAuthentication()
		.userDetailsContextMapper(new UserDetailsContextMapperImpl())
        .userDnPatterns("ou=People").userSearchFilter("(&(sAMAccountName={0}))") 
        .groupRoleAttribute("cn").groupSearchBase("ou=Groups").groupSearchFilter("(&(member={0}))")
        .contextSource(getLdapContextSource());              
		
	}
	
	private LdapContextSource getLdapContextSource() throws Exception {
        LdapContextSource cs = new LdapContextSource();
        cs.setUrl(Config.getLdapUrl());
        cs.setBase("DC=nexus,DC=csiro,DC=au");
        cs.setUserDn(Config.getUserDN());
        
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.REFERRAL, "follow");
        cs.setBaseEnvironmentProperties(env);
        cs.setPassword(Config.getLdapPassword());       
        cs.afterPropertiesSet();
        
        return cs;
    }
	
	protected class CustomSuccessHandler implements AuthenticationSuccessHandler{
		
		@Override  
	    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,  
	                                        HttpServletResponse httpServletResponse,  
	                                        Authentication authentication)  
	            throws IOException, ServletException {  
			
			HttpSession session = httpServletRequest.getSession();
			LdapUser authUser = (LdapUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			session.setAttribute("username", authUser.getUsername());
			session.setAttribute("authorities", authentication.getAuthorities());

			// set our response to OK status
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("text/html; charset=UTF-8");			
			Gson gson = new Gson();			
			httpServletResponse.getWriter().write(gson.toJson(authUser));
	    }  
		
		
	} 

}