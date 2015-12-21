package org.csiro.oai.igsn.binding;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.csiro.binding.IGSNJAXBInterface;

import org.csiro.igsn.entity.postgres2_0.CvSamplematerial;
import org.csiro.igsn.entity.postgres2_0.CvSampletype;
import org.csiro.igsn.entity.postgres2_0.Sample;
import org.csiro.igsn.entity.postgres2_0.SampleCollector;
import org.csiro.igsn.entity.postgres2_0.Samplecuration;
import org.csiro.igsn.entity.postgres2_0.Sampledfeatures;
import org.csiro.igsn.entity.postgres2_0.Sampleresources;
import org.csiro.igsn.entity.postgres2_0.Samplingfeatures;
import org.csiro.igsn.utilities.SpatialUtilities;
import org.csiro.oai.dc.binding.OaiDcType;
import org.csiro.oai.igsn.binding.Samples.Sample.MaterialTypes;
import org.csiro.oai.igsn.binding.Samples.Sample.SampleCollectors;
import org.csiro.oai.igsn.binding.Samples.Sample.SampleCollectors.Collector;
import org.csiro.oai.igsn.binding.Samples.Sample.SampleCuration.Curation;
import org.csiro.oai.igsn.binding.Samples.Sample.SampleTypes;
import org.csiro.oai.igsn.binding.Samples.Sample.SamplingLocation;
import org.csiro.oai.igsn.binding.Samples.Sample.SamplingMethod;
import org.csiro.oai.igsn.binding.Samples.Sample.SamplingTime;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


@Service
public class JAXBIGSNConverter implements IGSNJAXBInterface{
	
	ObjectFactory objectFactory;
	final String METAPREFIX="cs_igsn";
	final String NAMESPACE_FOR_BINDING="http://igsn.org/schema/kernel-v.1.0";
	final String SCHEMA_LOCATION_FOR_BINDING="https://raw.githubusercontent.com/kitchenprinzessin3880/csiro-igsn-schema/master/igsn-csiro-v2.0.xsd";
	final Class XML_ROOT_CLASS = org.csiro.oai.igsn.binding.Samples.Sample.class;
	
	public JAXBIGSNConverter(){
		this.objectFactory = new ObjectFactory();
	}
	

	
	public boolean supports(String metadataPrefix){
		if(metadataPrefix.toLowerCase().equals(this.METAPREFIX)){
			return true;
		}else{
			return false;
		}
			
	}
	
	
	//VT: This will always only wrap 1 sample to 1 samples as a binding to the oai
	public Samples convert(Sample sampleEntity){
		Calendar cal = Calendar.getInstance();
		
		Samples.Sample sampleXml = new Samples.Sample();
		Samples.Sample.SampleNumber sampleNumberXml = new Samples.Sample.SampleNumber();
		
		sampleNumberXml.setIdentifierType(IdentifierType.fromValue("igsn"));
		sampleNumberXml.setValue(sampleEntity.getIgsn());
		
		
		Samples.Sample.IsPublic isPublic = new Samples.Sample.IsPublic();
		isPublic.setValue(sampleEntity.getIspublic());
		sampleXml.setIsPublic(isPublic);
		

		sampleXml.setLandingPage(sampleEntity.getLandingpage());	
		sampleXml.setComments(sampleEntity.getComment());
		
		//VT: Set classification
		Samples.Sample.Classification classification = new Samples.Sample.Classification();
		if(sampleEntity.getClassificationidentifier()!=null)
			classification.setClassificationIdentifier(sampleEntity.getClassificationidentifier());
		if(sampleEntity.getClassification()!=null)
			classification.setValue(sampleEntity.getClassification());	
		if(classification.getClassificationIdentifier()!=null || classification.getValue()!=null)
			sampleXml.setClassification(classification);
		
		//VT: Log element - Not Needed
		Samples.Sample.LogElement logElement = new Samples.Sample.LogElement();
		logElement.setValue("Status:"+ sampleEntity.getStatusByRegistrationstatus().getStatuscode());		
		cal.setTime(new Date());				  
		logElement.setTimeStamp(String.valueOf(cal.get(Calendar.YEAR)));					
		logElement.setEvent(EventType.SUBMITTED);		
		sampleXml.setLogElement(logElement);
		
		//VT: Set Material Type
		Samples.Sample.MaterialTypes materialType = new Samples.Sample.MaterialTypes();
		JAXBElement<MaterialTypes> materialTypeJAXBElement = this.objectFactory.createSamplesSampleMaterialTypes(materialType);		
		for(CvSamplematerial sampleMaterial : sampleEntity.getCvSamplematerials()){
			if(NilReasonType.match(sampleMaterial.getMaterialidentifier())){
				materialType.setNilReason(sampleMaterial.getMaterialidentifier());
				materialTypeJAXBElement.setNil(true);
			}else{
				materialType.getMaterialType().add(sampleMaterial.getMaterialidentifier());
			}			
		}				
		sampleXml.setMaterialTypes(materialTypeJAXBElement);
		
		//VT: Set other name - DB store othername in 1:1 with sample
		if(sampleEntity.getOthername()!=null){
			Samples.Sample.OtherNames otherName = new Samples.Sample.OtherNames();
			otherName.getOtherName().add(sampleEntity.getOthername());
			sampleXml.setOtherNames(otherName);
		}
		
		sampleXml.setPurpose(sampleEntity.getPurpose());
		
		//VT: Set resource
		Samples.Sample.RelatedResources resources = new Samples.Sample.RelatedResources();		
		for(Sampleresources resourceEntity : sampleEntity.getSampleresourceses()){
			Samples.Sample.RelatedResources.RelatedResourceIdentifier resourceId= new Samples.Sample.RelatedResources.RelatedResourceIdentifier();			
			resourceId.setRelatedIdentifierType(RelatedIdentifierType.fromValue(resourceEntity.getCvRelatedIdentifiertype().getRelatedidentifiertype()));					
			resourceId.setRelationType(RelationType.fromValue(resourceEntity.getCvResourceRelationshiptype().getRelationshipType()));
			resourceId.setValue(resourceEntity.getResourceidentifier());
			resources.getRelatedResourceIdentifier().add(resourceId);			
		}
		if(!resources.getRelatedResourceIdentifier().isEmpty()){
			sampleXml.setRelatedResources(resources);
		}
		
		
		//VT: Set Sample Collector
		Samples.Sample.SampleCollectors sampleCollectors = new Samples.Sample.SampleCollectors();
		JAXBElement<SampleCollectors> sampleCollectorJAXBElement = this.objectFactory.createSamplesSampleSampleCollectors(sampleCollectors);
		for(SampleCollector sampleCollector: sampleEntity.getSampleCollectors()){
			if(NilReasonType.match(sampleCollector.getCollector())){
				sampleCollectors.setNilReason(sampleCollector.getCollector());
				sampleCollectorJAXBElement.setNil(true);
			}else{
				Collector collector = new Collector();
				collector.setCollectorIdentifier(sampleCollector.getCollectoridentifier());
				collector.setValue(sampleCollector.getCollector());
				sampleCollectors.getCollector().add(collector);
			}
			
		}		
		sampleXml.setSampleCollectors(sampleCollectorJAXBElement);
		
		//VT: Sample Curation
		Samples.Sample.SampleCuration sampleCurationXml = new Samples.Sample.SampleCuration();
		for(Samplecuration sc : sampleEntity.getSamplecurations()){
			Curation c= new Curation();
			
			c.setCurator(sc.getCurator());
			c.setCurationLocation(sc.getCurationlocation());
			
			//VT Set time - Linked to database curation start time
			Curation.CurationTime curationTime= new Curation.CurationTime();
			if(sc.getCurationstart()!=null){
				cal.setTime(sc.getCurationstart());
				curationTime.setTimeInstant(String.valueOf(cal.get(Calendar.YEAR)));			
				c.setCurationTime(curationTime);
			}
			
		
			//VT Set curator
			sampleCurationXml.getCuration().add(c);
		}				
		sampleXml.setSampleCuration(sampleCurationXml);		
		
		
		sampleXml.setSampleName(sampleEntity.getSamplename());
		sampleXml.setSampleNumber(sampleNumberXml);
						
		
		//VT: Sample Type
		Samples.Sample.SampleTypes sampleTypesXml = new Samples.Sample.SampleTypes();
		JAXBElement<SampleTypes> sampleTypeJAXBElement = this.objectFactory.createSamplesSampleSampleTypes(sampleTypesXml);
		for(CvSampletype cst : sampleEntity.getCvSampletypes()){
			if(NilReasonType.match(cst.getSampletypeidentifier())){
				sampleTypesXml.setNilReason(cst.getSampletypeidentifier());				
				sampleTypesXml.getSampleType().add(null);
				sampleTypeJAXBElement.setNil(true);
				break;
			}else{
				sampleTypesXml.getSampleType().add(cst.getSampletypeidentifier());
			}
		}										
		sampleXml.setSampleTypes(sampleTypeJAXBElement);		
		
		
		sampleXml.setSamplingCampaign(sampleEntity.getSamplingcampaign());
		
		
		//VT:Sampling Feature
		Samples.Sample.SamplingFeatures samplingFeatures = new Samples.Sample.SamplingFeatures();
		for(Samplingfeatures sampleFeature:sampleEntity.getSamplingfeatures()){
			
			Samples.Sample.SamplingFeatures.SamplingFeature feature = new Samples.Sample.SamplingFeatures.SamplingFeature();
			Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation featureLocation = new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation();
			Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Elevation elevation = new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Elevation();
			//VT: elevation
			elevation.setDatum(sampleFeature.getVerticaldatum());
			elevation.setUnits(sampleFeature.getElevationUnits());
			elevation.setValue(sampleFeature.getElevation());
			featureLocation.setElevation(elevation);
			
			//VT: wkt
			Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt wkt = new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt();
			wkt.setSrs(sampleFeature.getFeaturesrs());
			wkt.setSpatialType(getSpatialTypeFromGeometry(sampleFeature.getFeaturegeom()));
			wkt.setValue(sampleFeature.getFeaturegeom().toText());			
			featureLocation.setWkt(wkt);
			
			featureLocation.setLocality(sampleFeature.getFeaturelocality());
			
			feature.setSamplingFeatureLocation(featureLocation);
			Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureName samplingFeatureName = new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureName();
			samplingFeatureName.setValue(sampleFeature.getFeaturename());
			samplingFeatureName.setSamplingFeatureType(sampleFeature.getCvSamplingfeature().getIdentifier());
			feature.setSamplingFeatureName(samplingFeatureName);
			
			Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures sampledFeaturesXML = new Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures();
			for(Sampledfeatures sampledFeature:sampleFeature.getSampledfeatures()){
				
				Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures.SampledFeature SampledFeatureXML= new Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures.SampledFeature();
				SampledFeatureXML.setValue(sampledFeature.getFeaturename());
				SampledFeatureXML.setSampledFeatureType(sampledFeature.getFeaturetype());
				sampledFeaturesXML.getSampledFeature().add(SampledFeatureXML);
			}
			if(sampledFeaturesXML.getSampledFeature()!=null && !sampledFeaturesXML.getSampledFeature().isEmpty()){
				feature.setSampledFeatures(sampledFeaturesXML);
			}						
			samplingFeatures.getSamplingFeature().add(feature);
			
		}		
		if(!samplingFeatures.getSamplingFeature().isEmpty()){
			sampleXml.setSamplingFeatures(samplingFeatures);
		}
		
		//VT:Sampling location
		Samples.Sample.SamplingLocation samplingLocation = new Samples.Sample.SamplingLocation();	
		JAXBElement<SamplingLocation> samplingLocationJAXBElement = this.objectFactory.createSamplesSampleSamplingLocation(samplingLocation);
		
		if(sampleEntity.getSamplinglocNilreason()==null){
			Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Elevation elevation = new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Elevation();
			//VT: sample elevation
			elevation.setDatum(sampleEntity.getVerticaldatum());
			elevation.setUnits(sampleEntity.getElevationUnits());
			elevation.setValue(sampleEntity.getElevation());
			if(elevation.getDatum()!=null || elevation.getUnits() != null || elevation.getValue()!=null){
				samplingLocation.setElevation(elevation);
			}		
			samplingLocation.setLocality(sampleEntity.getLocality());
			
			Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt wkt = new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt();
			wkt.setSrs(sampleEntity.getSamplinglocsrs());
			wkt.setSpatialType(getSpatialTypeFromGeometry(sampleEntity.getSamplinglocgeom()));
			wkt.setValue(sampleEntity.getSamplinglocgeom().toText());
			samplingLocation.setWkt(wkt);
		}else{
			samplingLocation.setNilReason(sampleEntity.getSamplinglocNilreason());
			samplingLocationJAXBElement.setNil(true);
		}						
		sampleXml.setSamplingLocation(samplingLocationJAXBElement);
		

		//VT:SamplingMethod		
		Samples.Sample.SamplingMethod samplingMethod= new Samples.Sample.SamplingMethod();
		JAXBElement<SamplingMethod> samplingMethodJAXBElement = this.objectFactory.createSamplesSampleSamplingMethod(samplingMethod);
		if(NilReasonType.match(sampleEntity.getCvSamplingmethod().getMethodidentifier())){
			samplingMethod.setNilReason(sampleEntity.getCvSamplingmethod().getMethodidentifier());
			samplingMethodJAXBElement.setNil(true);
		}else{
			samplingMethod.setValue(sampleEntity.getCvSamplingmethod().getMethodidentifier());
		}		
		sampleXml.setSamplingMethod(samplingMethodJAXBElement);//VT: TODO- check null
		
		//VT:SamplingTime
		if(sampleEntity.getSamplingtimeNilreason()!=null){
			Samples.Sample.SamplingTime samplingTime = new Samples.Sample.SamplingTime();	
			samplingTime.setNilReason(sampleEntity.getSamplingtimeNilreason());
			JAXBElement<SamplingTime> samplingTimeJAXBElement = this.objectFactory.createSamplesSampleSamplingTime(samplingTime);
			samplingTimeJAXBElement.setNil(true);
			sampleXml.setSamplingTime(samplingTimeJAXBElement);			
		}else{	
			Samples.Sample.SamplingTime samplingTime = new Samples.Sample.SamplingTime();				
			cal.setTime(sampleEntity.getSamplingstart());			    
			samplingTime.setTimeInstant(String.valueOf(cal.get(Calendar.YEAR)));
			sampleXml.setSamplingTime(this.objectFactory.createSamplesSampleSamplingTime(samplingTime));
		}
		
		ObjectFactory objFact = new ObjectFactory();
		Samples samplesXml = objFact.createSamples();
		
		samplesXml.getSample().add(sampleXml);
		
		return samplesXml;
	}
	
	public static SpatialType getSpatialTypeFromGeometry(Geometry geometry){
		if(geometry instanceof Polygon){
			return SpatialType.POLYGON; 
		}else if(geometry instanceof Point){
			return SpatialType.POINT;
		}else{
			return null;
		}
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
