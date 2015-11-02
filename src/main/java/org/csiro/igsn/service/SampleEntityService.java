package org.csiro.igsn.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.csiro.igsn.bindings.allocation2_0.EventType;
import org.csiro.igsn.bindings.allocation2_0.IdentifierType;
import org.csiro.igsn.bindings.allocation2_0.ObjectFactory;
import org.csiro.igsn.bindings.allocation2_0.RelatedIdentifierType;
import org.csiro.igsn.bindings.allocation2_0.RelationType;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.igsn.bindings.allocation2_0.SpatialType;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCollectors.Collector;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCuration.Curator;
import org.csiro.igsn.entity.postgres2_0.CvSamplematerial;
import org.csiro.igsn.entity.postgres2_0.CvSampletype;
import org.csiro.igsn.entity.postgres2_0.Prefix;
import org.csiro.igsn.entity.postgres2_0.Registrant;
import org.csiro.igsn.entity.postgres2_0.Sample;
import org.csiro.igsn.entity.postgres2_0.SampleCollector;
import org.csiro.igsn.entity.postgres2_0.Samplecuration;
import org.csiro.igsn.entity.postgres2_0.Sampleresources;
import org.csiro.igsn.entity.postgres2_0.Samplingfeatures;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SampleEntityService {

	


	public ResponseEntity<? extends Object> getSampleMetadataByIGSN(String igsn) {
		
		
		Sample sampleEntity= this.search(igsn);
		if(sampleEntity==null){
			return new ResponseEntity<String>("IGSN does not exists in our database", HttpStatus.NOT_FOUND); 
		}
		
		ObjectFactory objFact = new ObjectFactory();
		Samples samplesXml = objFact.createSamples();
		
		samplesXml.getSample().add(this.SampleToXml(sampleEntity));
		return new ResponseEntity<Object>(samplesXml, HttpStatus.OK);

	}
	

	public Samples.Sample SampleToXml(Sample sampleEntity){
		
		Calendar cal = Calendar.getInstance();
		
		Samples.Sample sampleXml = new Samples.Sample();
		Samples.Sample.SampleNumber sampleNumberXml = new Samples.Sample.SampleNumber();
		
		sampleNumberXml.setIdentifierType(IdentifierType.fromValue("igsn"));
		sampleNumberXml.setValue(sampleEntity.getIgsn());
		
		
		
		sampleXml.setIsPublic(sampleEntity.getIspublic());
		sampleXml.setLandingPage(sampleEntity.getLandingpage());	
		sampleXml.setComments(sampleEntity.getComment());
		
		//VT: Set classification
		Samples.Sample.Classification classification = new Samples.Sample.Classification();
		classification.setClassificationIdentifier(sampleEntity.getClassificationidentifier());
		classification.setValue(sampleEntity.getClassification());		
		sampleXml.setClassification(classification);
		
		//VT: Log element
		Samples.Sample.LogElement logElement = new Samples.Sample.LogElement();
		logElement.setValue("log value dunno where");		
		cal.setTime(new Date());				  
		logElement.setTimeStamp(String.valueOf(cal.get(Calendar.YEAR)));					
		logElement.setEvent(EventType.SUBMITTED);		
		sampleXml.setLogElement(logElement);
		
		//VT: Set Material Type
		Samples.Sample.MaterialTypes materialType = new Samples.Sample.MaterialTypes();
		for(CvSamplematerial sampleMaterial : sampleEntity.getCvSamplematerials()){
			materialType.getMaterialType().add(sampleMaterial.getMaterialidentifier());
		}				
		sampleXml.setMaterialTypes(materialType);
		
		//VT: Set other name - DB store othername in 1:1 with sample
		Samples.Sample.OtherNames otherName = new Samples.Sample.OtherNames();
		otherName.getOtherName().add(sampleEntity.getOthername());
		sampleXml.setOtherNames(otherName);
		
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
		sampleXml.setRelatedResources(resources);
		
		
		//VT: Set Sample Collector
		Samples.Sample.SampleCollectors sampleCollectors = new Samples.Sample.SampleCollectors();
		for(SampleCollector sampleCollector: sampleEntity.getSampleCollectors()){
			Collector collector = new Collector();
			collector.setCollectorIdentifier(sampleCollector.getCollectoridentifier());
			collector.setValue(sampleCollector.getCollector());
			sampleCollectors.getCollector().add(collector);
		}		
		sampleXml.setSampleCollectors(sampleCollectors);
		
		//VT: Sample Curation
		Samples.Sample.SampleCuration sampleCurationXml = new Samples.Sample.SampleCuration();
		for(Samplecuration sc : sampleEntity.getSamplecurations()){
			Curator c= new Curator();
			
			c.setCuratorName(sc.getCurator());
			c.setCurationLocation(sc.getCurationlocation());
			
			//VT Set time - Linked to database curation start time
			Curator.CurationTime curationTime= new Curator.CurationTime();
			cal.setTime(sc.getCurationstart());
			curationTime.setTimeInstant(String.valueOf(cal.get(Calendar.YEAR)));			
			c.setCurationTime(curationTime);
		
			//VT Set curator
			sampleCurationXml.getCurator().add(c);
		}				
		sampleXml.setSampleCuration(sampleCurationXml);		
		
		
		sampleXml.setSampleName(sampleEntity.getSamplename());
		sampleXml.setSampleNumber(sampleNumberXml);
						
		
		//VT: Sample Type
		Samples.Sample.SampleTypes sampleTypesXml = new Samples.Sample.SampleTypes();		
		for(CvSampletype cst : sampleEntity.getCvSampletypes()){
			sampleTypesXml.getSampleType().add(cst.getSampletypeidentifier());
		}										
		sampleXml.setSampleTypes(sampleTypesXml);		
		
		
		sampleXml.setSamplingCampaign(sampleEntity.getSamplingcampaign());
		
		
		//VT:Sampling Feature
		Samples.Sample.SamplingFeatures samplingFeatures = new Samples.Sample.SamplingFeatures();
		for(Samplingfeatures sampleFeature:sampleEntity.getSamplingfeatures()){
			
			Samples.Sample.SamplingFeatures.Feature feature = new Samples.Sample.SamplingFeatures.Feature();
			Samples.Sample.SamplingFeatures.Feature.FeatureLocation featureLocation = new Samples.Sample.SamplingFeatures.Feature.FeatureLocation();
			Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Elevation elevation = new Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Elevation();
			//VT: elevation
			elevation.setDatum(sampleFeature.getVerticaldatum());
			elevation.setUnits("missing");
			elevation.setValue(sampleFeature.getElevation());
			featureLocation.setElevation(elevation);
			
			//VT: wkt
			Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Wkt wkt = new Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Wkt();
			wkt.setSrs(sampleFeature.getFeaturesrs());
			wkt.setSpatialType(SpatialType.POINT);
			wkt.setValue(sampleFeature.getFeaturegeom().getCoordinate().y + " " + sampleFeature.getFeaturegeom().getCoordinate().x);			
			featureLocation.setWkt(wkt);
			
			featureLocation.setLocality(sampleFeature.getFeaturelocality());
			
			feature.setFeatureLocation(featureLocation);
			feature.setFeatureName(sampleFeature.getFeaturename());
			feature.setFeatureType(sampleFeature.getCvSamplingfeature().getIdentifier());
			
			samplingFeatures.getFeature().add(feature);
			
		}						
		sampleXml.setSamplingFeatures(samplingFeatures);
		
		//VT:Sampling location
		Samples.Sample.SamplingLocation samplingLocation = new Samples.Sample.SamplingLocation();
		
		Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Elevation elevation = new Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Elevation();
		//VT: sample elevation
		elevation.setDatum(sampleEntity.getVerticaldatum());
		elevation.setUnits("missing");
		elevation.setValue(sampleEntity.getElevation());		
		samplingLocation.setElevation(elevation);
		
		samplingLocation.setLocality(sampleEntity.getLocality());
		
		Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Wkt wkt = new Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Wkt();
		wkt.setSrs(sampleEntity.getSamplinglocsrs());
		wkt.setSpatialType(SpatialType.POINT);
		wkt.setValue(sampleEntity.getSamplinglocgeom().getCoordinate().y + " " + sampleEntity.getSamplinglocgeom().getCoordinate().x);
		samplingLocation.setWkt(wkt);
				
		sampleXml.setSamplingLocation(samplingLocation);
				
		sampleXml.setSamplingMethod(sampleEntity.getCvSamplingmethod().getMethodidentifier());
		
		Samples.Sample.SamplingTime samplingTime = new Samples.Sample.SamplingTime();				
		cal.setTime(sampleEntity.getSamplingstart());			    
		samplingTime.setTimeInstant(String.valueOf(cal.get(Calendar.YEAR)));
		sampleXml.setSamplingTime(samplingTime);
		
		return sampleXml;
		
	}
	
	
	
	public Sample search(String igsn){
		try{
			EntityManager em = JPAEntityManager.createEntityManager();
			Sample result = em.createNamedQuery("Sample.search",Sample.class)
		    .setParameter("igsn", igsn)
		    .getSingleResult();
			 em.close();			 
			 return result;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}
	}
	
	
	
}
