package org.csiro.igsn.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.binding.web.response.SampleSummaryResponse;
import org.csiro.igsn.entity.postgres2_0.CvSampletype;
import org.csiro.igsn.entity.postgres2_0.Sample;
import org.csiro.igsn.service.ControlledValueEntityService;
import org.csiro.igsn.service.WebSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebSearchContrl {

	WebSearchService webSearchService;
	ControlledValueEntityService controlledValueEntityService;
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	public WebSearchContrl(WebSearchService webSearchService,ControlledValueEntityService controlledValueEntityService){
		this.webSearchService=webSearchService;
		this.controlledValueEntityService=controlledValueEntityService;
	}
	
	@RequestMapping(value = "search.do")
    public ResponseEntity<Object> search(            
            @RequestParam(required = false, value ="igsn") String igsn,
            @RequestParam(required = false, value ="sampleType") String sampleType,
            @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
            @RequestParam(required = false, value ="pageSize") Integer pageSize, 
            Principal user,
            HttpServletResponse response) {
    	
    	try{
    		List<Sample> results = webSearchService.search(igsn,sampleType,pageNumber,pageSize);
    		List<SampleSummaryResponse> responses= new ArrayList<SampleSummaryResponse>();
    		for(Sample s: results){
    			SampleSummaryResponse summaryResponse= new SampleSummaryResponse();
    			summaryResponse.setIgsn(s.getIgsn());
    			summaryResponse.setName(s.getLandingpage());
    			responses.add(summaryResponse);
    		}
		    return  new ResponseEntity<Object>(responses,HttpStatus.OK);    		
	    	
    	}catch(Exception e){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating",e.getMessage()),HttpStatus.BAD_REQUEST);
    	}
    }
	
	
	@RequestMapping(value = "searchCount.do")
    public ResponseEntity<Long> searchCount(            
            @RequestParam(required = false, value ="igsn") String igsn,
            @RequestParam(required = false, value ="sampleType") String sampleType,
            Principal user,
            HttpServletResponse response) {
    	
		try{    		
    		Long count = this.webSearchService.searchSampleCount(igsn, sampleType);
    		return  new ResponseEntity<Long>(count,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		return new ResponseEntity<Long>(new Long(0),HttpStatus.OK);
    	}
    }
	
	@RequestMapping(value = "getSampleType.do")
    public ResponseEntity<Object> search(            
            Principal user,
            HttpServletResponse response) {
    	
    	try{
    		List<CvSampletype> results = this.controlledValueEntityService.getCvSampletype();
    		
		    return  new ResponseEntity<Object>(results,HttpStatus.OK);    		
	    	
    	}catch(Exception e){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error retrieving sampletype list",e.getMessage()),HttpStatus.BAD_REQUEST);
    	}
    }
	
	
}
