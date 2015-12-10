package org.csiro.igsn.web.controllers;


import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.csiro.igsn.service.SampleEntityService;
import org.csiro.oai.OAIService;
import org.csiro.oai.binding.OAIPMHtype;
import org.csiro.oai.binding.VerbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/service")
public class OAIPMHCtrl {

	static final Logger log = Logger.getLogger(OAIPMHCtrl.class);
	OAIService oaiService;
	SampleEntityService sampleEntityService;

	@Autowired
	public OAIPMHCtrl(OAIService oaiService,SampleEntityService sampleEntityService){
		this.oaiService = oaiService;
		this.sampleEntityService = sampleEntityService;
	}

	
	@RequestMapping(value = "/oai")
	public  ResponseEntity<?> getOAI( 
			@RequestParam(required = true, value ="verb") String verb,
			@RequestParam(required = false, value ="identifier") String identifier,
			@RequestParam(required = false, value ="metadataPrefix") String metadataPrefix) throws DatatypeConfigurationException {
		
		if(verb.equals(VerbType.GET_RECORD.value())){		
			return new ResponseEntity<JAXBElement<OAIPMHtype>>(oaiService.getRecordOAI(identifier, metadataPrefix),HttpStatus.OK);
		}else{
			return new ResponseEntity<JAXBElement<OAIPMHtype>>(oaiService.getBadVerb(),HttpStatus.OK);
		}
	
	}
	

	

}
