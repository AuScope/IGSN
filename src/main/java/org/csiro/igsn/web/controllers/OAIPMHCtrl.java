package org.csiro.igsn.web.controllers;


import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;
import org.csiro.igsn.entity.postgres2_0.Sample;
import org.csiro.igsn.service.SampleEntityService;
import org.csiro.igsn.utilities.NullUtilities;
import org.csiro.oai.OAIService;
import org.csiro.oai.TokenResumption;
import org.csiro.oai.TokenResumptionService;
import org.csiro.oai.binding.OAIPMHtype;
import org.csiro.oai.binding.VerbType;
import org.csiro.oai.dc.binding.OaiDcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	TokenResumptionService tokenResumptionService;
	
	@Value("#{configProperties['OAI_CSIRO_IDENTIFIER_PREFIX']}")
	private String OAI_CSIRO_IDENTIFIER_PREFIX;

	@Autowired
	public OAIPMHCtrl(OAIService oaiService,SampleEntityService sampleEntityService){
		this.oaiService = oaiService;
		this.sampleEntityService = sampleEntityService;
		this.tokenResumptionService = new TokenResumptionService();
	}

	
	@RequestMapping(value = "/oai")
	public  void getOAI( 
			HttpServletResponse response,
			@RequestParam(required = true, value ="verb") String verb,
			@RequestParam(required = false, value ="identifier") String identifier,
			@RequestParam(required = false, value ="metadataPrefix") String metadataPrefix,
			@RequestParam(required = false, value ="from") String from,
			@RequestParam(required = false, value ="until") String until,
			@RequestParam(required = false, value ="resumptionToken") String resumptionToken) throws DatatypeConfigurationException, JAXBException, IOException, ParseException {
		
		response.setContentType("text/xml");
		
		if(verb.equals(VerbType.GET_RECORD.value())){	
			if(identifier == null || identifier.isEmpty() || metadataPrefix == null || metadataPrefix.isEmpty()){
				marshalToWrtier(oaiService.getBadArgument(),response.getWriter(),OAIPMHtype.class);
				return;
			}
			Sample sample = sampleEntityService.searchSampleByIGSN(identifier.replace(OAI_CSIRO_IDENTIFIER_PREFIX, ""));			
			marshalToWrtier(oaiService.getRecordOAI(sample, metadataPrefix),response.getWriter(),OAIPMHtype.class,OaiDcType.class, org.csiro.oai.igsn.binding.Samples.Sample.class);
		}else if(verb.equals(VerbType.LIST_RECORDS.value())){
			
			//VT: First entry no token
			if(resumptionToken==null || resumptionToken.isEmpty()){		
				Long size = sampleEntityService.getSampleSizeByDate(NullUtilities.parseDateYYYYMMDDAllowNull(from), NullUtilities.parseDateYYYYMMDDAllowNull(until));
				List<Sample> samples= sampleEntityService.searchSampleByDate(NullUtilities.parseDateYYYYMMDDAllowNull(from), NullUtilities.parseDateYYYYMMDDAllowNull(until), 0);				
				marshalToWrtier(oaiService.getListRecords(samples, metadataPrefix,from,until,size,null),
						response.getWriter(),OAIPMHtype.class,OaiDcType.class, org.csiro.oai.igsn.binding.Samples.Sample.class);				
			}else{
				TokenResumption token = this.tokenResumptionService.get(resumptionToken);
				if(token==null){
					marshalToWrtier(oaiService.getBadResumptionToken(VerbType.LIST_RECORDS),response.getWriter(),OAIPMHtype.class);
					return;
				}
				token.setPage(token.getPage()+1); //VT: going to the next page
				List<Sample> samples= sampleEntityService.searchSampleByDate(NullUtilities.parseDateYYYYMMDDAllowNull(token.getFrom()), NullUtilities.parseDateYYYYMMDDAllowNull(token.getUntil()), token.getPage());
				marshalToWrtier(oaiService.getListRecords(samples, token.getMetadataprefix(),token.getFrom(),token.getUntil(),token.getCompleteListSize(),token),
						response.getWriter(),OAIPMHtype.class,OaiDcType.class, org.csiro.oai.igsn.binding.Samples.Sample.class);
			}
												
		}else{			
			marshalToWrtier(oaiService.getBadVerb(),response.getWriter(),OAIPMHtype.class,OaiDcType.class);
		}
	
	}
	
	private void marshalToWrtier(JAXBElement<?> obj,Writer writer, Class... z) throws JAXBException{
		
		final Marshaller m = JAXBContext.newInstance(z).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);			
			m.marshal(obj, writer);
	}		

}
