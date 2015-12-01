package org.csiro.igsn.web.controllers;


import java.security.Principal;
import java.util.Set;

import org.apache.log4j.Logger;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample;
import org.csiro.igsn.entity.postgres2_0.Prefix;
import org.csiro.igsn.entity.postgres2_0.Registrant;
import org.csiro.igsn.service.ControlledValueEntityService;
import org.csiro.igsn.service.PrefixEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/subnamespace")//http://localhost:8080/CSIRO-IGSN/rest/employees
public class SubNameSpaceCtrl {

	static final Logger log = Logger.getLogger(SubNameSpaceCtrl.class);
	PrefixEntityService prefixEntityService;
	ControlledValueEntityService controlledValueEntityService;
	
	@Autowired
	public SubNameSpaceCtrl(PrefixEntityService prefixEntityService,ControlledValueEntityService controlledValueEntityService){
		this.prefixEntityService = prefixEntityService;
		this.controlledValueEntityService = controlledValueEntityService;
	}
	
	@RequestMapping(value = "/list/all")
	public  ResponseEntity<?> getAllSubnamespaces(Principal user) {
		ResponseEntity<?> response = null;		
		if (user != null) {						
			try {
				response = new ResponseEntity<Object>(prefixEntityService.listAllPrefix(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			response = new ResponseEntity<Object>("no login", HttpStatus.UNAUTHORIZED);
		}
		return response;
	}
	
	
	// This request registers the subnamespace.
		@RequestMapping(value = "/register/{prefix}", method = RequestMethod.GET)
		public ResponseEntity<? extends Object> registerSubNameSpace(
				@PathVariable("prefix") String prefix,Principal user) {			
			String usr = null;
			String pwd = null;			
			ResponseEntity<String> response = null;
			if (user!=null) {			
				usr = user.getName();
				
				Registrant registrant = controlledValueEntityService.searchRegistrant(usr);
				if(!prefixStartsWithAllowedPrefix(registrant.getAllocator().getPrefixes(),prefix)){
					return response = new ResponseEntity<String>("Prefix not authorized", HttpStatus.UNAUTHORIZED);
				}				
								
				try {
					response = (ResponseEntity<String>) prefixEntityService.registerPrefix(usr, pwd, prefix);
				} catch (Exception e) {
					e.printStackTrace();
					response = new ResponseEntity<String>("Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<String>("no login", HttpStatus.UNAUTHORIZED);
			}
			log.debug("SubNameSpace Registration Status: " + response.getStatusCode());
			return response;
		}
	
		private boolean prefixStartsWithAllowedPrefix(Set<Prefix> allowedPrefix,String s){
			boolean result = false;
			for(Prefix prefix:allowedPrefix){
				if(s.startsWith(prefix.getPrefix())){
					return true;
				};
			}
			return result;
		}
}
