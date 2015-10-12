package org.csiro.rockstore.security;


import java.util.Collection;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsContextMapperImpl extends LdapUserDetailsMapper {


	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		UserDetails userDetails= super.mapUserFromContext(ctx,username,authorities);
		String fullName = ctx.getStringAttribute("givenName");
		String email = ctx.getStringAttribute("mail");
		return new LdapUser((LdapUserDetails)userDetails,fullName,email);
	}
	

}