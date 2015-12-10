package org.csiro.oai;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.csiro.igsn.entity.postgres2_0.Sample;
import org.csiro.igsn.entity.postgres2_0.SampleCollector;
import org.csiro.igsn.entity.postgres2_0.Samplecuration;
import org.csiro.igsn.entity.postgres2_0.Sampleresources;
import org.csiro.igsn.service.SampleEntityService;
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
import org.csiro.oai.dc.binding.ElementType;
import org.csiro.oai.dc.binding.OaiDcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAIService {
	
	ObjectFactory oaiObjectFactory;
	SampleEntityService sampleEntityService;
	
	SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-ddTHH:mm:ssZ");
	
	@Value("#{configProperties['OAI_BASEURL_VALUE']}")
	private String OAI_BASEURL_VALUE;
	
	@Value("#{configProperties['OAI_BASEURL_VALUE']}")
	private String OAI_CSIRO_METADATAPREFIX;
	
	@Value("#{configProperties['OAI_CSIRO_IDENTIFIER_PREFIX']}")
	private String OAI_CSIRO_IDENTIFIER_PREFIX;
	
	@Autowired
	public OAIService(SampleEntityService sampleEntityService){
		this.sampleEntityService=sampleEntityService;
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
	
	public JAXBElement<OAIPMHtype> getRecordOAI(String identifier, String metadataPrefix) throws DatatypeConfigurationException{
		
		//VT: a identifier is madeup from OAI_CSIRO_IDENTIFIER_PREFIX + igsn.
		Sample sample = sampleEntityService.searchSampleByIGSN(identifier.replace(OAI_CSIRO_IDENTIFIER_PREFIX, ""));
		
		OAIPMHtype oaiType = oaiObjectFactory.createOAIPMHtype();
		
		//VT:Set response Date
		oaiType.setResponseDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		
		//VT:Set Request Type
		RequestType requestType = new RequestType();
		requestType.setVerb(VerbType.GET_RECORD);	
		requestType.setIdentifier(identifier);
		requestType.setMetadataPrefix(metadataPrefix);
		requestType.setValue(OAI_BASEURL_VALUE);
		oaiType.setRequest(requestType);
		
		//VT:GetRecord
		GetRecordType getRecordType = new GetRecordType();
		RecordType recordType = new RecordType();
		HeaderType headerType = new HeaderType();		
		
		//GetRecord header
		headerType.setIdentifier(identifier);
		headerType.setDatestamp(dateFormatter.format(sample.getModified()));
		if(sample.getStatusByRegistrationstatus().getStatuscode().equals("Deprecated")){
			headerType.setStatus(StatusType.DELETED);
		}				
		recordType.setHeader(headerType);	
		
		//VT: Set Metadata
		recordType.setMetadata(getDCMetaData(sample));
		
		getRecordType.setRecord(recordType);						
		oaiType.setGetRecord(getRecordType);		
		JAXBElement<OAIPMHtype> oaipmh = oaiObjectFactory.createOAIPMH(oaiType);
		return oaipmh;
	}
	
	public MetadataType getDCMetaData(Sample sample){
		
		org.csiro.oai.dc.binding.ObjectFactory dcObjectfactory = new org.csiro.oai.dc.binding.ObjectFactory();
		OaiDcType oaiDcType = new OaiDcType();
		//Title
		ElementType title = new ElementType();
		title.setValue(sample.getSamplename());
		oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createTitle(title));
		
		for(SampleCollector sampleCollector:sample.getSampleCollectors()){
			ElementType creator = new ElementType();
			creator.setValue(sampleCollector.getCollector());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createCreator(creator));
		}
		
		for(Samplecuration sampleCuration:sample.getSamplecurations()){
			ElementType publisher = new ElementType();
			publisher.setValue(sampleCuration.getCurator());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createPublisher(publisher));
		}
		
		ElementType subject = new ElementType();
		subject.setValue(sample.getClassification());
		oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createSubject(subject));
		
		ElementType description = new ElementType();
		description.setValue(sample.getComment());
		oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createDescription(description));
		
		for(Sampleresources resource:sample.getSampleresourceses()){
			ElementType relation = new ElementType();
			relation.setValue("RelatedResource: " + resource.getResourceidentifier() + ", relationship: " + resource.getCvResourceRelationshiptype().getRelationshipType());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createDescription(relation));				
		}
		
		
		if(sample.getSamplingstart()!=null){
			ElementType date = new ElementType();
			date.setValue(dateFormatter.format(sample.getSamplingstart()));
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createDate(date));
		}
		
		if(sample.getSamplinglocgeom()!=null){
			ElementType coverage = new ElementType();
			coverage.setValue("Coordinates(lat/Lon):" + sample.getSamplinglocgeom().getCoordinate().toString());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createCoverage(coverage));
		}
		
		ElementType type = new ElementType();
		type.setValue("http://purl.org/dc/dcmitype/PhysicalObject");
		oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createType(type));
		
		ElementType identifier = new ElementType();
		identifier.setValue(sample.getIgsn());
		oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createIdentifier(identifier));
		
		JAXBElement<OaiDcType> oaiDc = dcObjectfactory.createDc(oaiDcType);		
		MetadataType metaDataType = new MetadataType();
		metaDataType.setAny(oaiDc);
				
		return metaDataType;
	}
	
	public void getCGIGSNMetaData(){
		
	}

}
