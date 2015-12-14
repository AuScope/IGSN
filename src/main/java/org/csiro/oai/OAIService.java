package org.csiro.oai;

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
import org.csiro.oai.binding.GetRecordType;
import org.csiro.oai.binding.HeaderType;
import org.csiro.oai.binding.MetadataType;
import org.csiro.oai.binding.OAIPMHerrorType;
import org.csiro.oai.binding.OAIPMHerrorcodeType;
import org.csiro.oai.binding.OAIPMHtype;
import org.csiro.oai.binding.ObjectFactory;
import org.csiro.oai.binding.RecordType;
import org.csiro.oai.binding.RequestType;
import org.csiro.oai.binding.StatusType;
import org.csiro.oai.binding.VerbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;



public class OAIService {
	
	ObjectFactory oaiObjectFactory;
	
	
	SimpleDateFormat dateFormatterLong = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX");
	SimpleDateFormat dateFormatterShort = new SimpleDateFormat("YYYY-MM-dd");
	
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
	
	
	
	
	public JAXBElement<OAIPMHtype> getListRecords(List<Sample> sample, String metadataPrefix) throws DatatypeConfigurationException, JAXBException{
		
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
