package org.csiro.igsn.web.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres2_0.Allocator;
import org.csiro.igsn.entity.postgres2_0.Registrant;
import org.csiro.igsn.service.RegistrantEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegistrantCtrl {
	
	static final Logger log = Logger.getLogger(RegistrantCtrl.class);
	RegistrantEntityService registrantEntityService;
	
	@Autowired
	public RegistrantCtrl(RegistrantEntityService registrantEntityService){
		this.registrantEntityService = registrantEntityService;
	}
	
	
	@RequestMapping("getUser.do")
	public ResponseEntity<Principal>  user(Principal user) {
		Allocator authority = registrantEntityService.isAuthority(user.getName());
		if(authority!=null && !authority.getUsername().isEmpty()){
			return new ResponseEntity<Principal>(user,HttpStatus.OK);
		}else{
			return new ResponseEntity<Principal>(user,HttpStatus.UNAUTHORIZED);
		}
	}
	
	@RequestMapping("getAllRegistrant.do")
	public ResponseEntity<List<?>>  getAllRegistrant(Principal user) {
		if(user==null){
			return new ResponseEntity<List<?>>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<?>>(registrantEntityService.getAllRegistrant(),HttpStatus.OK);
		
	}
	
	@RequestMapping("addRegistrant.do")
	public ResponseEntity<List<?>>  addRegistrant( 
			@RequestParam(required = true, value ="name") String name,
			@RequestParam(required = true, value ="email") String email,
			@RequestParam(required = true, value ="username") String username,
			Principal user) {
		if(user==null){
			return new ResponseEntity<List<?>>(HttpStatus.UNAUTHORIZED);
		}
		
		try{
			Allocator allocator = registrantEntityService.isAuthority(user.getName());
			Registrant registrant = new Registrant( allocator,
					name, email, username,"");
			registrant.setCreated(new Date());
			registrant.setUpdated(new Date());
			registrant.setIsactive(true);
			registrantEntityService.persist(registrant);				
			return new ResponseEntity<List<?>>(registrantEntityService.getAllRegistrant(),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<List<?>>(HttpStatus.BAD_REQUEST);
		}
		
	}

}
