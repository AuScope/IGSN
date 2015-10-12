package org.csiro.rockstore.utilities;

import java.security.Principal;

import org.csiro.rockstore.security.LdapUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SecurityUtilities {
	
	public static LdapUser getLdapUser(Principal user){
		if(user==null){
			return null;
		}
		UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)user;
		LdapUser authUser = (LdapUser)authToken.getPrincipal();		
		return authUser;
	}

}
