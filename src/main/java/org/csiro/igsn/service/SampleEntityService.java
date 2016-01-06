package org.csiro.igsn.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.csiro.igsn.bindings.allocation2_0.EventType;
import org.csiro.igsn.bindings.allocation2_0.JAXBConverter;
import org.csiro.igsn.bindings.allocation2_0.ObjectFactory;
import org.csiro.igsn.bindings.allocation2_0.Samples;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.RelatedResources.RelatedResourceIdentifier;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCollectors.Collector;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SampleCuration.Curation;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingFeatures.SamplingFeature;
import org.csiro.igsn.bindings.allocation2_0.Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures.SampledFeature;
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

import com.vividsolutions.jts.geom.Geometry;

@Service
public class SampleEntityService {
	
	ControlledValueEntityService controlledValueEntityService;
	JAXBConverter jaxbConverter;
	
	public static final int PAGING_SIZE=10;
	
	@Autowired
	public SampleEntityService(ControlledValueEntityService controlledValueEntityService){
		this.controlledValueEntityService = controlledValueEntityService;
		this.jaxbConverter = new JAXBConverter();
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
		return this.jaxbConverter.convert(sampleEntity);		
	}
	
	
	public Long getSampleSizeByDate(Date fromDate, Date until) {
				
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();				
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<Sample> from = countQuery.from(Sample.class);				
					
		List<Predicate> predicates =this.oaiPredicateBuilder(fromDate,until, criteriaBuilder,from);
			
		CriteriaQuery<Long> select = countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<Long> typedQuery = em.createQuery(select);
		
		
	   Long result = typedQuery.getSingleResult();
		
		em.close();
		return result;
	}
	
	public List<Sample> searchSampleByDate(Date fromDate, Date until, Integer pageNumber){
		
		final Integer pageSize = getPagingSize();
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();				
		CriteriaQuery<Sample> criteriaQuery = criteriaBuilder.createQuery(Sample.class);
		Root<Sample> from = criteriaQuery.from(Sample.class);
		
		
					
		List<Predicate> predicates =this.oaiPredicateBuilder(fromDate,until, criteriaBuilder,from);
			
		CriteriaQuery<Sample> select = criteriaQuery.select(from).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		
		select = select.orderBy(criteriaBuilder.asc(from.get("sampleid")));
	
		TypedQuery<Sample> typedQuery = em.createQuery(select);
		
		if(pageNumber != null && pageSize != null){
			typedQuery.setFirstResult((pageNumber)*pageSize);
		    typedQuery.setMaxResults(pageSize);
		}

	    List<Sample> result = typedQuery.getResultList();
		
		em.close();
		return result;
	}
	
	
	private List<Predicate> oaiPredicateBuilder(Date from, Date until,CriteriaBuilder criteriaBuilder,Root<Sample> fromTable){
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		//VT: we are only keen in public date for oai harvesting
		predicates.add(criteriaBuilder.isTrue(fromTable.get("ispublic")));
		
		if (from != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(fromTable.get("modified"),from));
		}
		
		if (until != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(fromTable.get("modified"),until));
		}
		
		
		return predicates;
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
			String samplingLocationStrPoint = sampleXml.getSamplingLocation().getValue().getWkt().getValue();
			Geometry samplinglocgeom = SpatialUtilities.wktToGeometry(samplingLocationStrPoint);		
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
					Geometry featureLoc = null;
					try{
						String featureLocStrPoint = feature.getSamplingFeatureLocation().getWkt().getValue();										
						 featureLoc = SpatialUtilities.wktToGeometry(featureLocStrPoint);
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

	public int getPagingSize() {
		// TODO Auto-generated method stub
		return this.PAGING_SIZE;
	}

	
	
	
	
	
}
