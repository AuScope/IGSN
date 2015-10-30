package org.csiro.igsn.web.controllers;


import org.apache.log4j.Logger;
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
	
	@Autowired
	public SubNameSpaceCtrl(PrefixEntityService prefixEntityService){
		this.prefixEntityService = prefixEntityService;
	}
	
	@RequestMapping(value = "/list/all")
	public  ResponseEntity<?> getAllSubnamespaces() {
		
		//List<Prefix> registrantPrefixList = null;
		String usr = null;
		String pwd = null;
		ResponseEntity<?> response = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			usr = userDetails.getUsername();
			pwd = userDetails.getPassword();
		
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
				@PathVariable("prefix") String prefix) {
			
			String usr = null;
			String pwd = null;
			
			ResponseEntity<String> response = null;

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				usr = userDetails.getUsername();
				pwd = userDetails.getPassword();
				
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
	
	
}
