package org.csiro.igsn.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.xml.bind.JAXBElement;

import org.csiro.igsn.bindings.allocation2_0.EventType;
import org.csiro.igsn.bindings.allocation2_0.IdentifierType;
import org.csiro.igsn.bindings.allocation2_0.NilReasonType;
import org.csiro.igsn.bindings.allocation2_0.ObjectFactory;
import org.csiro.igsn.bindings.allocation2_0.RelatedIdentifierType;
import org.csiro.igsn.bindings.allocation2_0.RelationType;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.MaterialTypes;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.RelatedResources.RelatedResourceIdentifier;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCollectors;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCollectors.Collector;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCuration.Curation;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleTypes;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingFeatures.SamplingFeature;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures.SampledFeature;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingLocation;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingMethod;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingTime;
import org.csiro.igsn.bindings.allocation2_0.SpatialType;
import org.csiro.igsn.entity.postgres2_0.CvRelatedIdentifiertype;
import org.csiro.igsn.entity.postgres2_0.CvResourceRelationshiptype;
import org.csiro.igsn.entity.postgres2_0.CvSamplematerial;
import org.csiro.igsn.entity.postgres2_0.CvSampletype;
import org.csiro.igsn.entity.postgres2_0.CvSamplingfeature;
import org.csiro.igsn.entity.postgres2_0.CvSamplingmethod;
import org.csiro.igsn.entity.postgres2_0.Registrant;
import org.csiro.igsn.entity.postgres2_0.Sample;
import org.csiro.igsn.entity.postgres2_0.SampleCollector;
import org.csiro.igsn.entity.postgres2_0.Samplecuration;
import org.csiro.igsn.entity.postgres2_0.Sampledfeatures;
import org.csiro.igsn.entity.postgres2_0.Sampleresources;
import org.csiro.igsn.entity.postgres2_0.Samplingfeatures;
import org.csiro.igsn.entity.postgres2_0.Status;
import org.csiro.igsn.utilities.SpatialUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Point;

@Service
public class SampleEntityService {
	
	ControlledValueEntityService controlledValueEntityService;
	ObjectFactory objectFactory;
	
	@Autowired
	public SampleEntityService(ControlledValueEntityService controlledValueEntityService){
		this.controlledValueEntityService = controlledValueEntityService;
		this.objectFactory = new ObjectFactory();
	}

	public ResponseEntity<? extends Object> getSampleMetadataByIGSN(String igsn) throws Exception {
		
		
		Sample sampleEntity= this.searchSampleByIGSN(igsn);
		if(sampleEntity==null){
			return new ResponseEntity<String>("IGSN does not exists in our database", HttpStatus.NOT_FOUND); 
		}
		
		ObjectFactory objFact = new ObjectFactory();
		Samples samplesXml = objFact.createSamples();
		
		samplesXml.getSample().add(this.SampleToXml(sampleEntity));
		return new ResponseEntity<Object>(samplesXml, HttpStatus.OK);

	}
	

	public Samples.Sample SampleToXml(Sample sampleEntity) throws Exception{
		
		
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
			wkt.setSpatialType(SpatialType.POINT);
			wkt.setValue(sampleFeature.getFeaturegeom().getCoordinate().y + " " + sampleFeature.getFeaturegeom().getCoordinate().x);			
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
			wkt.setSpatialType(SpatialType.POINT);
			wkt.setValue(sampleEntity.getSamplinglocgeom().getCoordinate().y + " " + sampleEntity.getSamplinglocgeom().getCoordinate().x);
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
		
		return sampleXml;
		
	}
	
	
	
	public Sample searchSampleByIGSN(String igsn){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			Sample result = em.createNamedQuery("Sample.search",Sample.class)
		    .setParameter("igsn", igsn)
		    .getSingleResult();			 		
			 return result;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();	
		}
	}


	public void insertSample(org.csiro.igsn.bindings.allocation2_0.Samples.Sample sampleXml,String user) throws Exception {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		Sample sampleEntity = new Sample();
		try{
			em.getTransaction().begin();
			insertSample(sampleXml,user,em,sampleEntity);		
			Status status = searchStatusByName("Registered");
			sampleEntity.setStatusByRegistrationstatus(status);
			em.persist(sampleEntity);
			em.flush();
			em.getTransaction().commit();
		    em.close();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}

	}
	/**
	 * 
	 * @param sampleXml
	 * @param user
	 * @param em
	 * @param sampleEntity - May not need this in the future
	 * @param update - true if this is a update else false to insert new
	 * @throws Exception
	 */
	private void insertSample(org.csiro.igsn.bindings.allocation2_0.Samples.Sample sampleXml,String user, EntityManager em,Sample sampleEntity) throws Exception{
		
		boolean isUpdate = sampleXml.getLogElement().getEvent().equals(EventType.UPDATED)?true:false;
		
		DateFormat df =new SimpleDateFormat("yyyy");
		
		//VT:Sampling Method		
		CvSamplingmethod samplingMethod = null;
		if(sampleXml.getSamplingMethod().isNil()){
			samplingMethod = controlledValueEntityService.search(sampleXml.getSamplingMethod().getValue().getNilReason());
		}else{
			samplingMethod = controlledValueEntityService.search(sampleXml.getSamplingMethod().getValue().getValue());
		}
		if(samplingMethod == null){
			samplingMethod = new CvSamplingmethod(sampleXml.getSamplingMethod().getValue().getValue(),"");
			em.persist(samplingMethod);//VT:TODO sort this out, Sampling Method should be a controlled list.
		}			
		sampleEntity.setCvSamplingmethod(samplingMethod);
		
		
		
		sampleEntity.setSamplename(sampleXml.getSampleName());
		try{
			sampleEntity.setOthername(sampleXml.getOtherNames().getOtherName().get(0));//VT database only support 1 other name;
		}catch(NullPointerException e){
			sampleEntity.setOthername(null);
		}
		
		sampleEntity.setIgsn(sampleXml.getSampleNumber().getValue());
		sampleEntity.setLandingpage(sampleXml.getLandingPage());
		try{
			sampleEntity.setClassification(sampleXml.getClassification().getValue());
			sampleEntity.setClassificationidentifier(sampleXml.getClassification().getClassificationIdentifier());
		}catch(NullPointerException e){
			//VT: Do nothing because field is not mandatory
		}
		
	
		sampleEntity.setPurpose(sampleXml.getPurpose());
		
		//VT: SamplingLocation
		if(sampleXml.getSamplingLocation().isNil()){
			sampleEntity.setSamplinglocNilreason(sampleXml.getSamplingLocation().getValue().getNilReason());
		}else{
			String[] samplingLocationStrPoint = sampleXml.getSamplingLocation().getValue().getWkt().getValue().split(" ");
			Point samplinglocgeom = (Point)(SpatialUtilities.wktToGeometry(samplingLocationStrPoint[0], samplingLocationStrPoint[1]));		
			sampleEntity.setSamplinglocgeom(samplinglocgeom);
			sampleEntity.setSamplinglocsrs(sampleXml.getSamplingLocation().getValue().getWkt().getSrs());
			
			if(sampleXml.getSamplingLocation().getValue().getElevation()!=null){
				sampleEntity.setElevation(sampleXml.getSamplingLocation().getValue().getElevation().getValue());
			}else{
				sampleEntity.setElevation(null);
			}
			
			if(sampleXml.getSamplingLocation().getValue().getElevation()!=null){
				sampleEntity.setVerticaldatum(sampleXml.getSamplingLocation().getValue().getElevation().getDatum());
			}else{
				sampleEntity.setVerticaldatum(null);
			}	
			
			if(sampleXml.getSamplingLocation().getValue().getElevation()!=null){
				sampleEntity.setElevationUnits(sampleXml.getSamplingLocation().getValue().getElevation().getUnits());
			}else{
				sampleEntity.setElevationUnits(null);
			}
			sampleEntity.setLocality(sampleXml.getSamplingLocation().getValue().getLocality());
		}
		
		
		//VT: SamplingTime
		if(sampleXml.getSamplingTime().isNil()){
			sampleEntity.setSamplingtimeNilreason(sampleXml.getSamplingTime().getValue().getNilReason());
		}else{
			sampleEntity.setSamplingstart(df.parse(sampleXml.getSamplingTime().getValue().getTimeInstant()));
		}
			
		
		
		sampleEntity.setSamplingcampaign(sampleXml.getSamplingCampaign());
		
		
		
		sampleEntity.setComment(sampleXml.getComments());
		
				
		//VT:Registrant
		Registrant registrant = controlledValueEntityService.searchRegistrant(user);		
		sampleEntity.setRegistrant(registrant);
		if(!isUpdate){
			sampleEntity.setCreated(new Date());
		}
		sampleEntity.setModified(new Date());
		sampleEntity.setIspublic(sampleXml.getIsPublic().isValue());
		
		
		//VT: Sample types
		Set<CvSampletype> cvSampletypes = new HashSet<CvSampletype>();
		if(sampleXml.getSampleTypes().isNil()){
			cvSampletypes.add(controlledValueEntityService.searchSampleType(sampleXml.getSampleTypes().getValue().getNilReason()));
			sampleEntity.setCvSampletypes(cvSampletypes);
		}else{
			for(String sampleType:sampleXml.getSampleTypes().getValue().getSampleType()){			
				cvSampletypes.add(controlledValueEntityService.searchSampleType(sampleType));
			}
			sampleEntity.setCvSampletypes(cvSampletypes);
		}

		
		//VT:Curator
		Set<Samplecuration> samplecurations=new HashSet<Samplecuration>();
		for(Curation curator:sampleXml.getSampleCuration().getCuration()){
			if(curator!=null){
				samplecurations.add(new Samplecuration(sampleEntity,curator.getCurationLocation(),curator.getCurator(),
						curator.getCurationTime()==null || curator.getCurationTime().getTimeInstant()==null?null:df.parse(curator.getCurationTime().getTimeInstant()),
						null,""));
			}
		}
		if(samplecurations.isEmpty()){
			sampleEntity.setSamplecurations(null);
		}else{
			sampleEntity.setSamplecurations(samplecurations);
		}
		
		
		//VT: SamplingFeatures
		Set<Samplingfeatures> samplingfeatures = new HashSet<Samplingfeatures>();
		if(sampleXml.getSamplingFeatures()!=null){
			for(SamplingFeature feature:sampleXml.getSamplingFeatures().getSamplingFeature()){
				if(feature !=null){
					CvSamplingfeature cvSamplingfeature = controlledValueEntityService.searchSamplingfeatureByIdentifier(feature.getSamplingFeatureName().getSamplingFeatureType());
					Point featureLoc = null;
					try{
						String[] featureLocStrPoint = feature.getSamplingFeatureLocation().getWkt().getValue().split(" ");										
						 featureLoc = (Point)(SpatialUtilities.wktToGeometry(featureLocStrPoint[0], featureLocStrPoint[1]));
					}catch(Exception e){
						
					}
					
					 Set<Sampledfeatures> sampledfeatures = new HashSet<Sampledfeatures>();
					 if(feature.getSampledFeatures()!=null){
						 for(SampledFeature sampledFeatureXml: feature.getSampledFeatures().getSampledFeature()){
							 sampledfeatures.add(new Sampledfeatures(sampledFeatureXml.getSampledFeatureType(), sampledFeatureXml.getValue()));
						 }
					 }
							 
					samplingfeatures.add(new Samplingfeatures(cvSamplingfeature,feature.getSamplingFeatureName().getValue(),featureLoc,
							feature.getSamplingFeatureLocation()==null?null:feature.getSamplingFeatureLocation().getWkt().getSrs(),
							feature.getSamplingFeatureLocation()==null || feature.getSamplingFeatureLocation().getElevation()==null?null:feature.getSamplingFeatureLocation().getElevation().getValue(),
							feature.getSamplingFeatureLocation()==null || feature.getSamplingFeatureLocation().getElevation()==null?null:feature.getSamplingFeatureLocation().getElevation().getDatum(),
							feature.getSamplingFeatureLocation()==null ?null:feature.getSamplingFeatureLocation().getLocality(),
							feature.getSamplingFeatureLocation()==null || feature.getSamplingFeatureLocation().getElevation()==null?null:feature.getSamplingFeatureLocation().getElevation().getUnits(),
							sampledfeatures.isEmpty()?null:sampledfeatures));
				}
			}
		}
		if(samplingfeatures.isEmpty()){
			sampleEntity.setSamplingfeatures(null);
		}else{
			sampleEntity.setSamplingfeatures(samplingfeatures);
		}		
		
		
		//VT:SampleCollector
		Set<SampleCollector> sampleCollectors = new HashSet<SampleCollector>();
		if(sampleXml.getSampleCollectors().isNil()){
			sampleCollectors.add(new SampleCollector(sampleEntity,sampleXml.getSampleCollectors().getValue().getNilReason()));
			sampleEntity.setSampleCollectors(sampleCollectors);
		}else{
			for(Collector sampleCollector:sampleXml.getSampleCollectors().getValue().getCollector()){
				if(sampleCollector != null){
					sampleCollectors.add(new SampleCollector(sampleEntity,sampleCollector.getValue(),sampleCollector.getCollectorIdentifier()));
				}
			}
			sampleEntity.setSampleCollectors(sampleCollectors);
		}
		
		
		//VT:SampleResource
		Set<Sampleresources> sampleresourceses = new HashSet<Sampleresources>();
		if(sampleXml.getRelatedResources()!=null && sampleXml.getRelatedResources().getRelatedResourceIdentifier()!=null){
			for(RelatedResourceIdentifier resource:sampleXml.getRelatedResources().getRelatedResourceIdentifier()){					
				CvRelatedIdentifiertype cvRelatedIdentifiertype=controlledValueEntityService.searchRelatedIdentifier(resource.getRelatedIdentifierType().value());
				CvResourceRelationshiptype cvResourceRelationshiptype = controlledValueEntityService.searchByRelationshipType(resource.getRelationType().value());					
				sampleresourceses.add(new Sampleresources(cvRelatedIdentifiertype,sampleEntity,resource.getValue(),cvResourceRelationshiptype,new Date()));
			}
		}
		
		sampleEntity.setSampleresourceses(sampleresourceses);			
		
		
		//VT:SampleMaterial
		Set<CvSamplematerial> cvSamplematerials = new HashSet<CvSamplematerial>();	
		if(sampleXml.getMaterialTypes().isNil()){
			cvSamplematerials.add(controlledValueEntityService.searchByMaterialidentifier(sampleXml.getMaterialTypes().getValue().getNilReason()));
			sampleEntity.setCvSamplematerials(cvSamplematerials);
		}else{
			for(String sampleMaterial:sampleXml.getMaterialTypes().getValue().getMaterialType()){ 
				cvSamplematerials.add(controlledValueEntityService.searchByMaterialidentifier(sampleMaterial));
			}
			sampleEntity.setCvSamplematerials(cvSamplematerials);
		}
		
		

	}
	
	public Status searchStatusByName(String statuscode){
		try{
			EntityManager em = JPAEntityManager.createEntityManager();
			Status result = em.createNamedQuery("Status.searchStatusByName",Status.class)
		    .setParameter("statuscode", statuscode)
		    .getSingleResult();
			 em.close();			 
			 return result;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}
	}

	public void destroySample(org.csiro.igsn.bindings.allocation2_0.Samples.Sample sample) {
		EntityManager em = JPAEntityManager.createEntityManager();		
		try{
			em.getTransaction().begin();			
			Sample s = this.searchSampleByIGSN(sample.getSampleNumber().getValue());
			Status status = searchStatusByName("Destroyed");
			s.setStatusByPhysicalsamplestatus(status);
			em.merge(s);
			em.flush();
			em.getTransaction().commit();		    
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}finally{
			em.close();
		}
		
	}
	
	
	
	
	
	
	public void deprecateSample(org.csiro.igsn.bindings.allocation2_0.Samples.Sample sample) {
		EntityManager em = JPAEntityManager.createEntityManager();		
		try{
			em.getTransaction().begin();			
			Sample s = this.searchSampleByIGSN(sample.getSampleNumber().getValue());
			Status status = searchStatusByName("Deprecated");
			s.setStatusByRegistrationstatus(status);
			em.merge(s);
			em.flush();
			em.getTransaction().commit();		    
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}finally{
			em.close();
		}		
	}

	public void updateSample(org.csiro.igsn.bindings.allocation2_0.Samples.Sample sampleXml, String user) throws Exception {
		EntityManager em = JPAEntityManager.createEntityManager();
		Sample sampleEntity = this.searchSampleByIGSN(sampleXml.getSampleNumber().getValue());
		if(sampleEntity==null){
			throw new Exception("Sample not found, unable to update: Change logElement event to submitted to add new record.");
		}
		try{
			em.getTransaction().begin();
			insertSample(sampleXml,user,em,sampleEntity);		
			Status status = searchStatusByName("Updated");
			sampleEntity.setStatusByRegistrationstatus(status);
			em.merge(sampleEntity);
			em.flush();
			em.getTransaction().commit();
		    em.close();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}
		
	}

	public void testInsertSample(
			org.csiro.igsn.bindings.allocation2_0.Samples.Sample sampleXml, String user) throws Exception {
		
		if(!sampleXml.getLogElement().getEvent().equals(EventType.SUBMITTED)){
			throw new Exception("You can only test insert with log event type = submitted");
		}
		
		EntityManager em = JPAEntityManager.createEntityManager();
		Sample sampleEntity = new Sample();
		try{
			em.getTransaction().begin();
			insertSample(sampleXml,user,em,sampleEntity);		
			Status status = searchStatusByName("Registered");
			sampleEntity.setStatusByRegistrationstatus(status);
			em.persist(sampleEntity);
			em.flush();
			em.getTransaction().rollback();//VT: Because this is a test, it will always be rolled back
		    em.close();
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			throw e;
		}
	}
	
	
	
	
}
