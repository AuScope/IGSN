package org.csiro.igsn.web.controllers;

import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.igsn.service.PrefixEntityService;
import org.csiro.igsn.service.SampleEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/metadata")
public class MetaDataCtrl {
	
	static final Logger log = Logger.getLogger(MetaDataCtrl.class);
	SampleEntityService sampleEntityService;
	
	@Autowired
	public MetaDataCtrl(SampleEntityService sampleEntityService){
		this.sampleEntityService = sampleEntityService;
	}
	
	
	@RequestMapping(value = "/retrieve/{igsn}")
	public ResponseEntity getMetadataByIGSN(@PathVariable("igsn") String sampleNumber) {
		ResponseEntity<? extends Object> response = null;
		log.info("Get Metadata By Sample Id : " + sampleNumber);
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			userDetails.getUsername();
			userDetails.getPassword();
			try {
				response = sampleEntityService.getSampleMetadataByIGSN(sampleNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			response = new ResponseEntity<String>("no login", new HttpHeaders(),HttpStatus.UNAUTHORIZED);
		}
		log.info("Get Metadata By Sample Id - Response code : " + response.getStatusCode());

		return response;
	}

}
