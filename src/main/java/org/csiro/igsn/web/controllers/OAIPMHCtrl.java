package org.csiro.igsn.web.controllers;


import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
			@RequestParam(required = false, value ="resumptionToken") String resumptionToken,
			@RequestParam(required = false, value ="set") String set) throws DatatypeConfigurationException, JAXBException, IOException, ParseException {
		
		response.setContentType("text/xml");
		
		if(verb.equals(VerbType.GET_RECORD.value())){	
			if(identifier == null || identifier.isEmpty() || metadataPrefix == null || metadataPrefix.isEmpty()){
				marshalToWrtier(oaiService.getBadArgument(VerbType.GET_RECORD),response.getWriter(),OAIPMHtype.class);
				return;
			}
			Sample sample = sampleEntityService.searchPublicSampleByIGSN(identifier.replace(OAI_CSIRO_IDENTIFIER_PREFIX, ""));			
			marshalToWrtier(oaiService.getRecordOAI(sample, metadataPrefix),response.getWriter(),OAIPMHtype.class,oaiService.getSuitableConverter(metadataPrefix).getXMLRootClass());
		}else if(verb.equals(VerbType.LIST_RECORDS.value())){
			
			if(set!=null){
				marshalToWrtier(oaiService.getNoSetHierarchy(VerbType.LIST_RECORDS),response.getWriter(),OAIPMHtype.class);
				return;
			}
			
			//VT: First entry no token
			if(resumptionToken==null || resumptionToken.isEmpty()){		
				if(metadataPrefix==null){
					marshalToWrtier(oaiService.getBadArgument(VerbType.LIST_RECORDS),response.getWriter(),OAIPMHtype.class);
					return;
				}
				Long size = sampleEntityService.getSampleSizeByDate(NullUtilities.parseDateYYYYMMDDAllowNull(from), NullUtilities.parseDateYYYYMMDDAllowNull(until));
				List<Sample> samples= sampleEntityService.searchSampleByDate(NullUtilities.parseDateYYYYMMDDAllowNull(from), NullUtilities.parseDateYYYYMMDDAllowNull(until), 0);				
				marshalToWrtier(oaiService.getListRecords(samples, metadataPrefix,from,until,size,null),
						response.getWriter(),OAIPMHtype.class,oaiService.getSuitableConverter(metadataPrefix).getXMLRootClass());				
			}else{
				TokenResumption token = this.tokenResumptionService.get(resumptionToken);
				if(token==null){
					marshalToWrtier(oaiService.getBadResumptionToken(VerbType.LIST_RECORDS),response.getWriter(),OAIPMHtype.class);
					return;
				}
				token.setPage(token.getPage()+1); //VT: going to the next page
				List<Sample> samples= sampleEntityService.searchSampleByDate(NullUtilities.parseDateYYYYMMDDAllowNull(token.getFrom()), NullUtilities.parseDateYYYYMMDDAllowNull(token.getUntil()), token.getPage());
				marshalToWrtier(oaiService.getListRecords(samples, token.getMetadataprefix(),token.getFrom(),token.getUntil(),token.getCompleteListSize(),token),
						response.getWriter(),OAIPMHtype.class,oaiService.getSuitableConverter(token.getMetadataprefix()).getXMLRootClass());
			}
												
		}else if(verb.equals(VerbType.LIST_IDENTIFIERS.value())){
			
			if(set!=null){
				marshalToWrtier(oaiService.getNoSetHierarchy(VerbType.LIST_IDENTIFIERS),response.getWriter(),OAIPMHtype.class);
				return;
			}
			
			//VT: First entry no token
			if(resumptionToken==null || resumptionToken.isEmpty()){	
				if(metadataPrefix==null){
					marshalToWrtier(oaiService.getBadArgument(VerbType.LIST_IDENTIFIERS),response.getWriter(),OAIPMHtype.class);
					return;
				}
				Long size = sampleEntityService.getSampleSizeByDate(NullUtilities.parseDateYYYYMMDDAllowNull(from), NullUtilities.parseDateYYYYMMDDAllowNull(until));
				List<Sample> samples= sampleEntityService.searchSampleByDate(NullUtilities.parseDateYYYYMMDDAllowNull(from), NullUtilities.parseDateYYYYMMDDAllowNull(until), 0);				
				marshalToWrtier(oaiService.getListIdentifier(samples, metadataPrefix,from,until,size,null),
						response.getWriter(),OAIPMHtype.class);				
			}else{
				TokenResumption token = this.tokenResumptionService.get(resumptionToken);
				if(token==null){
					marshalToWrtier(oaiService.getBadResumptionToken(VerbType.LIST_RECORDS),response.getWriter(),OAIPMHtype.class);
					return;
				}
				token.setPage(token.getPage()+1); //VT: going to the next page
				List<Sample> samples= sampleEntityService.searchSampleByDate(NullUtilities.parseDateYYYYMMDDAllowNull(token.getFrom()), NullUtilities.parseDateYYYYMMDDAllowNull(token.getUntil()), token.getPage());
				marshalToWrtier(oaiService.getListIdentifier(samples, token.getMetadataprefix(),token.getFrom(),token.getUntil(),token.getCompleteListSize(),token),
						response.getWriter(),OAIPMHtype.class);
			}
												
		}else if(verb.equals(VerbType.LIST_METADATA_FORMATS.value())){
			if(identifier==null){
				marshalToWrtier(oaiService.getListMetadataFormat(null,identifier),response.getWriter(),OAIPMHtype.class);
				return;
			}
			Sample sample = sampleEntityService.searchSampleByIGSN(identifier.replace(OAI_CSIRO_IDENTIFIER_PREFIX, ""));
			marshalToWrtier(oaiService.getListMetadataFormat(sample,identifier),response.getWriter(),OAIPMHtype.class);
		}else if(verb.equals(VerbType.IDENTIFY.value())){			
			marshalToWrtier(oaiService.getIdentify(),response.getWriter(),OAIPMHtype.class);
		}else if(verb.equals(VerbType.LIST_SETS.value())){			
			marshalToWrtier(oaiService.getNoSetHierarchy(VerbType.LIST_SETS),response.getWriter(),OAIPMHtype.class);
		}else{			
			marshalToWrtier(oaiService.getBadVerb(),response.getWriter(),OAIPMHtype.class);
		}
	
	}
	
	private void marshalToWrtier(JAXBElement<?> obj,Writer writer, Class... z) throws JAXBException{
		
		final Marshaller m = JAXBContext.newInstance(z).createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);			
			m.marshal(obj, writer);
	}		

}
