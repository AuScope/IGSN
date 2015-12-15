package org.csiro.oai;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.csiro.binding.IGSNJAXBInterface;
import org.csiro.igsn.entity.postgres2_0.Sample;
import org.csiro.igsn.service.SampleEntityService;
import org.csiro.oai.binding.GetRecordType;
import org.csiro.oai.binding.HeaderType;
import org.csiro.oai.binding.ListRecordsType;
import org.csiro.oai.binding.MetadataType;
import org.csiro.oai.binding.OAIPMHerrorType;
import org.csiro.oai.binding.OAIPMHerrorcodeType;
import org.csiro.oai.binding.OAIPMHtype;
import org.csiro.oai.binding.ObjectFactory;
import org.csiro.oai.binding.RecordType;
import org.csiro.oai.binding.RequestType;
import org.csiro.oai.binding.ResumptionTokenType;
import org.csiro.oai.binding.StatusType;
import org.csiro.oai.binding.VerbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;



public class OAIService {
	
	ObjectFactory oaiObjectFactory;
	TokenResumptionService tokenResumptionService;
	
	SimpleDateFormat dateFormatterLong = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	SimpleDateFormat dateFormatterShort = new SimpleDateFormat("yyyy-MM-dd");
	
	List<IGSNJAXBInterface> igsnJAXBInterface;
	
	@Value("#{configProperties['OAI_BASEURL_VALUE']}")
	private String OAI_BASEURL_VALUE;
	
	@Value("#{configProperties['OAI_BASEURL_VALUE']}")
	private String OAI_CSIRO_METADATAPREFIX;
	
	@Value("#{configProperties['OAI_CSIRO_IDENTIFIER_PREFIX']}")
	private String OAI_CSIRO_IDENTIFIER_PREFIX;
	
	
	@Autowired
	public OAIService(List<IGSNJAXBInterface> igsnJAXBInterface){
		oaiObjectFactory = new ObjectFactory();
		this.igsnJAXBInterface = igsnJAXBInterface;
		this.tokenResumptionService = new TokenResumptionService();
	}
	 
	
	public JAXBElement<OAIPMHtype> getBadResumptionToken(VerbType operation) throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(operation);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.BAD_VERB);
		errorType.setValue("Expired or corrupted resumption token");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getBadVerb() throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.GET_RECORD);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.BAD_VERB);
		errorType.setValue("Illegal OAI Verb");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getBadArgument() throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.GET_RECORD);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
		errorType.setValue("Missing require arguement");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getNoRecordMatch(VerbType operation) throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(operation);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.NO_RECORDS_MATCH);
		errorType.setValue("Unable to find a matching record");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getCannotDisseminateFormat() throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.GET_RECORD);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT);
		errorType.setValue("metadataPrefix unrecognized");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getIdDoesNotExist() throws DatatypeConfigurationException{
		
		
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.GET_RECORD);		
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT: Set error
		OAIPMHerrorType errorType = new OAIPMHerrorType();
		errorType.setCode(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST);
		errorType.setValue("Unable to find ID");
		oaiType.getError().add(errorType);
		

		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public JAXBElement<OAIPMHtype> getRecordOAI(Sample sample, String metadataPrefix) throws DatatypeConfigurationException, JAXBException{
		
		if(sample==null){
			return this.getIdDoesNotExist();
		}
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.GET_RECORD);	
		requestType.setIdentifier(OAI_CSIRO_IDENTIFIER_PREFIX + sample.getIgsn());
		requestType.setMetadataPrefix(metadataPrefix);
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT:GetRecord
		GetRecordType getRecordType = new GetRecordType();
		RecordType recordType = new RecordType();
		HeaderType headerType = new HeaderType();		
		
		//GetRecord header
		headerType.setIdentifier(OAI_CSIRO_IDENTIFIER_PREFIX + sample.getIgsn());
		headerType.setDatestamp(dateFormatterShort.format(sample.getModified()));
		if(sample.getStatusByRegistrationstatus().getStatuscode().equals("Deprecated")){
			headerType.setStatus(StatusType.DELETED);
		}				
		recordType.setHeader(headerType);	
		
		//VT: Set Metadata
		IGSNJAXBInterface converter = this.getSuitableConverter(metadataPrefix);
		if(converter==null){
			return this.getCannotDisseminateFormat();
		}
		recordType.setMetadata(getMetaData(converter,sample));
		
		getRecordType.setRecord(recordType);						
		oaiType.setGetRecord(getRecordType);		
		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	
	
	
	public JAXBElement<OAIPMHtype> getListRecords(List<Sample> samples, String metadataPrefix, String from, String until, Long totalCount, TokenResumption tokenResumption) throws DatatypeConfigurationException, JAXBException{
		
		if(samples.isEmpty()){
			return this.getNoRecordMatch(VerbType.LIST_RECORDS);
		}
		
		//VT: Find suitable converter
		IGSNJAXBInterface converter = this.getSuitableConverter(metadataPrefix);
		if(converter==null){
			return this.getCannotDisseminateFormat();
		}
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.LIST_RECORDS);
		if(tokenResumption == null){
			if(from != null && !from.isEmpty()){
				requestType.setFrom(from);
			}		
			if(until !=null && !until.isEmpty()){
				requestType.setUntil(until);
			}
			requestType.setMetadataPrefix(metadataPrefix);
		}else{
			requestType.setResumptionToken(tokenResumption.getKey());
		}
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT:GetRecord
		ListRecordsType listRecordsType = new ListRecordsType();
		
		for(Sample sample : samples){
			RecordType recordType = new RecordType();
			HeaderType headerType = new HeaderType();		
			
			//GetRecord header
			headerType.setIdentifier(OAI_CSIRO_IDENTIFIER_PREFIX + sample.getIgsn());
			headerType.setDatestamp(dateFormatterShort.format(sample.getModified()));
			if(sample.getStatusByRegistrationstatus()!=null && sample.getStatusByRegistrationstatus().getStatuscode().equals("Deprecated")){
				headerType.setStatus(StatusType.DELETED);
			}				
			recordType.setHeader(headerType);	
			
			
			recordType.setMetadata(getMetaData(converter,sample));
			
			listRecordsType.getRecord().add(recordType);	
		}
		
		
		listRecordsType.setResumptionToken(manageResumptionToken( metadataPrefix,  from,  until,  totalCount,  tokenResumption));
		oaiType.setListRecords(listRecordsType);	
		
		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	
	
	private ResumptionTokenType manageResumptionToken(
			String metadataPrefix, String from, String until, Long totalCount,
			TokenResumption tokenResumption) throws DatatypeConfigurationException {

		//VT: Less record than the paging size therefore no token needed.
		if(totalCount <= SampleEntityService.PAGING_SIZE){
			return null;
		}
		
		//VT: First entry without toekn and totalCount > than paging therefore generate token
		if(tokenResumption == null){
			TokenResumption token = new TokenResumption();
			token.setCompleteListSize(totalCount);
			token.setCursor(0);
			token.setPage(0);
			token.setFrom(from);
			token.setUntil(until);
			token.setMetadataprefix(metadataPrefix);
			String key = tokenResumptionService.put(token);
			
			ResumptionTokenType rtt = new ResumptionTokenType();
			rtt.setCompleteListSize(BigInteger.valueOf(token.getCompleteListSize().intValue()));
			rtt.setCursor(BigInteger.valueOf(token.getCursor()));
			rtt.setExpirationDate(tokenResumptionService.getExpiration(key));
			rtt.setValue(key);
			
			return rtt;
		}else{
			//VT: sequent entry has token					
			tokenResumption.setCursor(tokenResumption.getPage() * SampleEntityService.PAGING_SIZE);
			
			if(tokenResumption.getPage() * SampleEntityService.PAGING_SIZE >= totalCount - 1){
				//VT this is the end of the page
				ResumptionTokenType rtt = new ResumptionTokenType();
				rtt.setCompleteListSize(BigInteger.valueOf(tokenResumption.getCompleteListSize().intValue()));
				rtt.setCursor(BigInteger.valueOf(tokenResumption.getCursor()));								
				return rtt;
			}else{
				
				
			
				tokenResumptionService.update(tokenResumption.getKey(),tokenResumption);
				
				ResumptionTokenType rtt = new ResumptionTokenType();
				rtt.setCompleteListSize(BigInteger.valueOf(tokenResumption.getCompleteListSize().intValue()));
				rtt.setCursor(BigInteger.valueOf(tokenResumption.getCursor()));
				rtt.setExpirationDate(tokenResumptionService.getExpiration(tokenResumption.getKey()));
				rtt.setValue(tokenResumption.getKey());
				return rtt;
			}
		}
		
		
		
		
		
	}


	public MetadataType getMetaData(IGSNJAXBInterface converter,Sample sample) throws JAXBException{

		MetadataType metaDataType = new MetadataType();
		metaDataType.setAny(converter.convert(sample));
				
		return metaDataType;
	}
	
	
	
	private  IGSNJAXBInterface getSuitableConverter(String metadataPrefix){
		for(IGSNJAXBInterface converter:this.igsnJAXBInterface){
			if(converter.supports(metadataPrefix)){
				return converter;
			}
		}
				
		return null;
	}

	
}
