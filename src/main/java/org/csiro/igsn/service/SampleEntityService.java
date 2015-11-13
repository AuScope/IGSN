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

import org.csiro.igsn.bindings.allocation2_0.EventType;
import org.csiro.igsn.bindings.allocation2_0.IdentifierType;
import org.csiro.igsn.bindings.allocation2_0.ObjectFactory;
import org.csiro.igsn.bindings.allocation2_0.RelatedIdentifierType;
import org.csiro.igsn.bindings.allocation2_0.RelationType;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.RelatedResources.RelatedResourceIdentifier;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCollectors.Collector;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCuration.Curator;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingFeatures.Feature;
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
	
	@Autowired
	public SampleEntityService(ControlledValueEntityService controlledValueEntityService){
		this.controlledValueEntityService = controlledValueEntityService;
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
		
		
		
		sampleXml.setIsPublic(sampleEntity.getIspublic());
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
		for(CvSamplematerial sampleMaterial : sampleEntity.getCvSamplematerials()){
			materialType.getMaterialType().add(sampleMaterial.getMaterialidentifier());
		}				
		sampleXml.setMaterialTypes(materialType);
		
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
			if(sc.getCurationstart()!=null){
				cal.setTime(sc.getCurationstart());
				curationTime.setTimeInstant(String.valueOf(cal.get(Calendar.YEAR)));			
				c.setCurationTime(curationTime);
			}
			
		
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
			elevation.setUnits(sampleFeature.getElevationUnits());
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
		if(!samplingFeatures.getFeature().isEmpty()){
			sampleXml.setSamplingFeatures(samplingFeatures);
		}
		
		//VT:Sampling location
		Samples.Sample.SamplingLocation samplingLocation = new Samples.Sample.SamplingLocation();
		
		Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Elevation elevation = new Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Elevation();
		//VT: sample elevation
		elevation.setDatum(sampleEntity.getVerticaldatum());
		elevation.setUnits(sampleEntity.getElevationUnits());
		elevation.setValue(sampleEntity.getElevation());
		if(elevation.getDatum()!=null || elevation.getUnits() != null || elevation.getValue()!=null){
			samplingLocation.setElevation(elevation);
		}
		
		samplingLocation.setLocality(sampleEntity.getLocality());
		
		Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Wkt wkt = new Samples.Sample.SamplingFeatures.Feature.FeatureLocation.Wkt();
		wkt.setSrs(sampleEntity.getSamplinglocsrs());
		wkt.setSpatialType(SpatialType.POINT);
		wkt.setValue(sampleEntity.getSamplinglocgeom().getCoordinate().y + " " + sampleEntity.getSamplinglocgeom().getCoordinate().x);
		samplingLocation.setWkt(wkt);
				
		sampleXml.setSamplingLocation(samplingLocation);
				
		sampleXml.setSamplingMethod(sampleEntity.getCvSamplingmethod().getMethodidentifier());
		if(sampleEntity.getSamplingstart()!=null){
			Samples.Sample.SamplingTime samplingTime = new Samples.Sample.SamplingTime();				
			cal.setTime(sampleEntity.getSamplingstart());			    
			samplingTime.setTimeInstant(String.valueOf(cal.get(Calendar.YEAR)));
			sampleXml.setSamplingTime(samplingTime);
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
		CvSamplingmethod samplingMethod = controlledValueEntityService.search(sampleXml.getSamplingMethod());
		if(samplingMethod == null){
			samplingMethod = new CvSamplingmethod(sampleXml.getSamplingMethod(),"");
			em.persist(samplingMethod);//VT:TODO sort this out, Sampling Method should be a controlled list.
		}
		sampleEntity.setCvSamplingmethod(samplingMethod);
		sampleEntity.setSamplename(sampleXml.getSampleName());
		try{
			sampleEntity.setOthername(sampleXml.getOtherNames().getOtherName().get(0));//VT database only support 1 other name;
		}catch(NullPointerException e){
			//VT: Do nothing because field is not mandatory
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
		
		
		String[] samplingLocationStrPoint = sampleXml.getSamplingLocation().getWkt().getValue().split(" ");
		Point samplinglocgeom = (Point)(SpatialUtilities.wktToGeometry(samplingLocationStrPoint[0], samplingLocationStrPoint[1]));		
		sampleEntity.setSamplinglocgeom(samplinglocgeom);
		sampleEntity.setSamplinglocsrs(sampleXml.getSamplingLocation().getWkt().getSrs());
		
		if(sampleXml.getSamplingLocation().getElevation()!=null){
			sampleEntity.setElevation(sampleXml.getSamplingLocation().getElevation().getValue());
		}else{
			sampleEntity.setElevation(null);
		}
		
		if(sampleXml.getSamplingLocation().getElevation()!=null){
			sampleEntity.setVerticaldatum(sampleXml.getSamplingLocation().getElevation().getDatum());
		}else{
			sampleEntity.setVerticaldatum(null);
		}	
		
		if(sampleXml.getSamplingLocation().getElevation()!=null){
			sampleEntity.setElevationUnits(sampleXml.getSamplingLocation().getElevation().getUnits());
		}else{
			sampleEntity.setElevationUnits(null);
		}
		
		
		sampleEntity.setLocality(sampleXml.getSamplingLocation().getLocality());
		
		sampleEntity.setSamplingstart(df.parse(sampleXml.getSamplingTime().getTimeInstant()));	
		
		
		sampleEntity.setSamplingcampaign(sampleXml.getSamplingCampaign());
		
		
		
		sampleEntity.setComment(sampleXml.getComments());
		
				
		//VT:Registrant
		Registrant registrant = controlledValueEntityService.searchRegistrant(user);		
		sampleEntity.setRegistrant(registrant);
		if(!isUpdate){
			sampleEntity.setCreated(new Date());
		}
		sampleEntity.setModified(new Date());
		sampleEntity.setIspublic(sampleXml.isIsPublic());
		
	
		
		//VT: Sample types
		Set<CvSampletype> cvSampletypes = new HashSet<CvSampletype>();
		for(String sampleType:sampleXml.getSampleTypes().getSampleType()){
			cvSampletypes.add(controlledValueEntityService.searchSampleType(sampleType));
		}
		if(cvSampletypes.isEmpty()){
			sampleEntity.setCvSampletypes(null);
		}else{
			sampleEntity.setCvSampletypes(cvSampletypes);
		}
		
		
		
		//VT:Curator
		Set<Samplecuration> samplecurations=new HashSet<Samplecuration>();
		for(Curator curator:sampleXml.getSampleCuration().getCurator()){
			if(curator!=null){
				samplecurations.add(new Samplecuration(sampleEntity,curator.getCurationLocation(),curator.getCuratorName(),
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
			for(Feature feature:sampleXml.getSamplingFeatures().getFeature()){
				if(feature !=null){
					CvSamplingfeature cvSamplingfeature = controlledValueEntityService.searchSamplingfeatureByIdentifier(feature.getFeatureType());
					Point featureLoc = null;
					try{
						String[] featureLocStrPoint = feature.getFeatureLocation().getWkt().getValue().split(" ");										
						 featureLoc = (Point)(SpatialUtilities.wktToGeometry(featureLocStrPoint[0], featureLocStrPoint[1]));
					}catch(Exception e){
						
					}					
					samplingfeatures.add(new Samplingfeatures(cvSamplingfeature,feature.getFeatureName(),featureLoc,
							feature.getFeatureLocation()==null?null:feature.getFeatureLocation().getWkt().getSrs(),
							feature.getFeatureLocation()==null || feature.getFeatureLocation().getElevation()==null?null:feature.getFeatureLocation().getElevation().getValue(),
							feature.getFeatureLocation()==null || feature.getFeatureLocation().getElevation()==null?null:feature.getFeatureLocation().getElevation().getDatum(),
							feature.getFeatureLocation()==null ?null:feature.getFeatureLocation().getLocality(),
							feature.getFeatureLocation()==null || feature.getFeatureLocation().getElevation()==null?null:feature.getFeatureLocation().getElevation().getUnits()));
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
		for(Collector sampleCollector:sampleXml.getSampleCollectors().getCollector()){
			if(sampleCollector != null){
				sampleCollectors.add(new SampleCollector(sampleEntity,sampleCollector.getValue(),sampleCollector.getCollectorIdentifier()));
			}
		}
		if(sampleCollectors.isEmpty()){
			sampleEntity.setSampleCollectors(null);
		}else{
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
		for(String sampleMaterial:sampleXml.getMaterialTypes().getMaterialType()){
			cvSamplematerials.add(controlledValueEntityService.searchByMaterialidentifier(sampleMaterial));
		}
		if(cvSamplematerials.isEmpty()){
			sampleEntity.setCvSamplematerials(null);
		}else{
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
