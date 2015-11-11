package org.csiro.igsn.web.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample;
import org.csiro.igsn.entity.postgres2_0.Prefix;
import org.csiro.igsn.exception.MintErrorCode;
import org.csiro.igsn.exception.MintEventLog;
import org.csiro.igsn.service.MintService;
import org.csiro.igsn.service.PrefixEntityService;
import org.csiro.igsn.service.SampleEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@RestController
@RequestMapping(value = "/igsn")
public class IGSNMintCtrl {
	
	final Logger log = Logger.getLogger(IGSNMintCtrl.class);
	SampleEntityService sampleEntityService;
	MintService mintService;
	PrefixEntityService prefixEntityService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");



	
	@Value("#{configProperties['IGSN_CSIRO_XSD_URL']}")
	private String IGSN_CSIRO_XSD_URL;

	
	
	@Autowired
	public IGSNMintCtrl(SampleEntityService sampleEntityService,MintService mintService,PrefixEntityService prefixEntityService){
		this.sampleEntityService = sampleEntityService;
		this.mintService = mintService;
		this.prefixEntityService = prefixEntityService;
	}
	
	
	@RequestMapping(value = "/test/mint", method = { RequestMethod.POST, RequestMethod.HEAD }, consumes = { "application/xml" })
	public  ResponseEntity<?> mintTest(@RequestBody String samples) {
		//this.mint(samples,true);
		return null;
	}
	
	@RequestMapping(value = "/mint", method = { RequestMethod.POST, RequestMethod.HEAD }, consumes = { "application/xml" })
	public  ResponseEntity<?> mint(@RequestBody Samples samples) {
		this.mint(samples,false);
		return null;
	}
	
	public void mint(Samples samples, boolean test){
		ResponseEntity<String> responseCode = null;
		boolean isXMLValid = true;
		boolean isPrefixValid = true;
		Schema schema = null;

		// 2. VALIDATE XML ====================================================
	
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {				
			URL schemaUrl = new URL(this.IGSN_CSIRO_XSD_URL);				
			schema = sf.newSchema(schemaUrl);
		} catch (SAXException e) {
			e.printStackTrace();
			//TODO
		} catch (MalformedURLException e) {
			log.error("URL malformed for schema location. Recheck config.properties file again.");
		}

		try {			
			JAXBContext jc = JAXBContext.newInstance(Samples.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setSchema(schema);		
			marshaller.marshal(samples, new DefaultHandler());		
		} catch (JAXBException e) {
			e.printStackTrace();
			isXMLValid = false;
			responseCode = new ResponseEntity<String>("XML validation is failed : " + e.getLocalizedMessage(),
					HttpStatus.BAD_REQUEST);
		}
		
		// 3. VALIDATE SUBNAMESPACE BASED ON USER NAME
				// =============================
		String usr = null;
		if (isXMLValid) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			usr = userDetails.getUsername();
			List<MintEventLog> mintEventLogs = new ArrayList<MintEventLog>();
			
			Set<Prefix> allowedPrefix = prefixEntityService.searchByUser(usr);
			
			
			for (Sample s : samples.getSample()) {
				MintEventLog mintEventLog= new MintEventLog(s.getSampleNumber().getValue());
				if(allowedPrefix.contains(s)){
					//String mintStatus = this.mintService.createRegistryXML(s.getSampleNumber().getValue(), s.getLandingPage(), sdf.format(new Date()), test, s.getLogElement().getEvent().value());
					//if (mintStatus.contains("OK")) {
					if(true){
						sampleEntityService.insertSample(s,usr);
						mintEventLog.setLog(MintErrorCode.SUCCESS, null);
					} else {
						mintEventLog.setLog(MintErrorCode.MINT_FAILURE, null);
					}
					
					
				}else{
					mintEventLog.setLog(MintErrorCode.PREFIX_UNREGISTERED, null);
				}
			}
			
		}

	
	}

}
