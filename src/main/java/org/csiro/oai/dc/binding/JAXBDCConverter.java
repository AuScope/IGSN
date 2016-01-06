package org.csiro.oai.dc.binding;

import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBElement;

import org.csiro.binding.IGSNJAXBInterface;
import org.csiro.igsn.entity.postgres2_0.CvSamplematerial;
import org.csiro.igsn.entity.postgres2_0.CvSampletype;
import org.csiro.igsn.entity.postgres2_0.Sample;
import org.csiro.igsn.entity.postgres2_0.SampleCollector;
import org.csiro.igsn.entity.postgres2_0.Samplecuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class JAXBDCConverter implements IGSNJAXBInterface{
	
	final String METAPREFIX="oai_dc";
	final String NAMESPACE_FOR_BINDING="http://www.openarchives.org/OAI/2.0/oai_dc/";
	final String SCHEMA_LOCATION_FOR_BINDING="http://www.openarchives.org/OAI/2.0/oai_dc.xsd";
	final Class XML_ROOT_CLASS = OaiDcType.class;
	
	@Value("#{configProperties['IGSN_HANDLE_PREFIX']}")
	private String IGSN_HANDLE_PREFIX;
	
	
	SimpleDateFormat dateFormatterLong = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX");
	
	
	public boolean supports(String metadataPrefix){
		if(metadataPrefix.toLowerCase().equals(this.METAPREFIX)){
			return true;
		}else{
			return false;
		}
			
	}
		
	
	
	public JAXBElement<OaiDcType> convert(Sample sample){
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
		
		for(CvSamplematerial materialType:sample.getCvSamplematerials()){
			ElementType type = new ElementType();
			type.setValue(materialType.getMaterialidentifier());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createFormat(type));
		}
		
//		for(Sampleresources resource:sample.getSampleresourceses()){
//			ElementType relation = new ElementType();
//			relation.setValue("RelatedResource: " + resource.getResourceidentifier() + ", relationship: " + resource.getCvResourceRelationshiptype().getRelationshipType());
//			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createDescription(relation));				
//		}
		
		
		if(sample.getSamplingstart()!=null){
			ElementType date = new ElementType();
			date.setValue(dateFormatterLong.format(sample.getSamplingstart()));
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createDate(date));
		}
		
		if(sample.getSamplinglocgeom()!=null){
			ElementType coverage = new ElementType();
			coverage.setValue("Coordinates(lon/Lat):" + sample.getSamplinglocgeom().getCoordinate().toString());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createCoverage(coverage));
		}
		
		for(CvSampletype sampleType:sample.getCvSampletypes()){
			ElementType type = new ElementType();
			type.setValue(sampleType.getSampletypeidentifier());
			oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createType(type));
		}
		
		
		ElementType identifier = new ElementType();
		identifier.setValue(IGSN_HANDLE_PREFIX + sample.getIgsn());
		oaiDcType.getTitleOrCreatorOrSubject().add(dcObjectfactory.createIdentifier(identifier));
		
		return dcObjectfactory.createDc(oaiDcType);	
	}



	@Override
	public String getMetadataPrefix() {		
		return METAPREFIX;
	}



	@Override
	public String getNamespace() {
		return NAMESPACE_FOR_BINDING;
	}



	@Override
	public String getSchemaLocation() {
		
		return SCHEMA_LOCATION_FOR_BINDING;
	}



	@Override
	public Class getXMLRootClass() {
		
		return XML_ROOT_CLASS;
	}





}
