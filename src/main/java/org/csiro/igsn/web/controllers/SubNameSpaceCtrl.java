package org.csiro.igsn.web.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.csiro.igsn.entity.postgres.Prefix;

@RestController
@RequestMapping(value = "/subnamespace")//http://localhost:8080/CSIRO-IGSN/rest/employees
public class SubNameSpaceCtrl {

	static final Logger log = Logger.getLogger(SubNameSpaceCtrl.class);
	
	@RequestMapping(value = "/all")
	public  ResponseEntity<Prefix> getAllSubnamespaces() {
		
		List<Prefix> registrantPrefixList = null;
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
				response = new ResponseEntity<>(dataServices.getSubnamespaces(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			response = new ResponseEntity<Object>("no login", new HttpHeaders(),HttpStatus.UNAUTHORIZED);
		}
		return response;
	}
	
	
}
