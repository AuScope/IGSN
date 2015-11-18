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
import org.csiro.igsn.bindings.allocation2_0.EventType;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample;
import org.csiro.igsn.entity.postgres2_0.Prefix;
import org.csiro.igsn.exception.DatabaseErrorCode;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
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
	

	
	
	@RequestMapping(value = "/test/mint", method = { RequestMethod.POST, RequestMethod.HEAD })
	public  ResponseEntity<?> mintTest(@RequestBody Samples samples) {
		ResponseEntity<?> result=this.mint(samples,true);
		return result;
	}
	
	@RequestMapping(value = "/mint", method = { RequestMethod.POST, RequestMethod.HEAD } )
	public  ResponseEntity<?> mint(@RequestBody Samples samples) {
		
		return this.mint(samples,false);
		
	}
	
	public ResponseEntity<?> mint(Samples samples, boolean test){
		
		boolean isXMLValid = true;
		
		Schema schema = null;

		// 2. VALIDATE XML ====================================================
	
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {				
			URL schemaUrl = new URL(this.IGSN_CSIRO_XSD_URL);				
			schema = sf.newSchema(schemaUrl);
		} catch (SAXException e) {
			e.printStackTrace();
			return   new ResponseEntity<String>("Failure retriving schema : " + e.getLocalizedMessage(),
					HttpStatus.BAD_REQUEST);
		} catch (MalformedURLException e) {
			log.error("URL malformed for schema location. Recheck config.properties file again.");
			return  new ResponseEntity<String>("Failure retriving schema : " + e.getLocalizedMessage(),
					HttpStatus.BAD_REQUEST);	
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
			return new ResponseEntity<String>("XML validation failed : " + e.getLocalizedMessage(),
					HttpStatus.BAD_REQUEST);			
		}
		
		// 3. VALIDATE SUBNAMESPACE BASED ON USER NAME
				// =============================
		String usr = null;
		List<MintEventLog> mintEventLogs = new ArrayList<MintEventLog>();
		boolean containsError=false;
		if (isXMLValid) {			
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			usr = userDetails.getUsername();
			
			
			Set<Prefix> allowedPrefix = prefixEntityService.searchByUser(usr);			
			
			for (Sample s : samples.getSample()) {
				MintEventLog mintEventLog= new MintEventLog(s.getSampleNumber().getValue());
				if(sampleStartsWithAllowedPrefix(allowedPrefix,s)){		
					if(s.getLogElement().getEvent().equals(EventType.SUBMITTED)||s.getLogElement().getEvent().equals(EventType.UPDATED)){
						try{
							String igsn=this.mintService.createRegistryXML(s.getSampleNumber().getValue(), s.getLandingPage(), sdf.format(new Date()), test, s.getLogElement().getEvent().value());							
							mintEventLog.setMintLog(MintErrorCode.MINT_SUCCESS, null);
							mintEventLog.setHandle("http://hdl.handle.net/"+igsn);							
						}catch(Exception e){
							mintEventLog.setMintLog(MintErrorCode.MINT_FAILURE, e.getMessage());
							mintEventLogs.add(mintEventLog);
							containsError=true;
							continue;
						}
					}
					
					try{
						if(test){
							sampleEntityService.testInsertSample(s,usr);
						}else if(s.getLogElement().getEvent().equals(EventType.SUBMITTED)){
							sampleEntityService.insertSample(s,usr);
						}else if(s.getLogElement().getEvent().equals(EventType.DESTROYED)){
							sampleEntityService.destroySample(s);
						}else if(s.getLogElement().getEvent().equals(EventType.DEPRECATED)){
							sampleEntityService.deprecateSample(s);
						}else if(s.getLogElement().getEvent().equals(EventType.UPDATED)){
							sampleEntityService.updateSample(s,usr);
						}
						mintEventLog.setDatabaseLog(DatabaseErrorCode.UPDATE_SUCCESS, null);
						mintEventLogs.add(mintEventLog);
					}catch(Exception e){
						mintEventLog.setDatabaseLog(DatabaseErrorCode.UPDATE_ERROR, e.getMessage());
						mintEventLogs.add(mintEventLog);
						containsError=true;
					}											
					
				}else{
					mintEventLog.setMintLog(MintErrorCode.PREFIX_UNREGISTERED, null);
					containsError=true;
				}
			}
			
		}
		if(containsError){
			return new ResponseEntity<List<MintEventLog>>(mintEventLogs,HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<List<MintEventLog>>(mintEventLogs,HttpStatus.OK);
		}
		

	
	}
	
	private boolean sampleStartsWithAllowedPrefix(Set<Prefix> allowedPrefix,Sample s){
		boolean result = false;
		for(Prefix prefix:allowedPrefix){
			if(s.getSampleNumber().getValue().startsWith(prefix.getPrefix())){
				return true;
			};
		}
		return result;
	}

}
