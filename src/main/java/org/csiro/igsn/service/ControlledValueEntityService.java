package org.csiro.igsn.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.csiro.igsn.entity.postgres2_0.CvRelatedIdentifiertype;
import org.csiro.igsn.entity.postgres2_0.CvResourceRelationshiptype;
import org.csiro.igsn.entity.postgres2_0.CvSamplematerial;
import org.csiro.igsn.entity.postgres2_0.CvSampletype;
import org.csiro.igsn.entity.postgres2_0.CvSamplingfeature;
import org.csiro.igsn.entity.postgres2_0.CvSamplingmethod;
import org.csiro.igsn.entity.postgres2_0.Registrant;
import org.springframework.stereotype.Service;

@Service
public class ControlledValueEntityService {
	

	public CvSamplingmethod search(String samplingMethod){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			CvSamplingmethod result = em.createNamedQuery("CvSamplingmethod.search",CvSamplingmethod.class)
		    .setParameter("methodidentifier", samplingMethod)
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
	
	public List<CvSampletype> getCvSampletype(){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			List<CvSampletype> result = em.createNamedQuery("CvSampletype.getList",CvSampletype.class)
		    .getResultList();			
			 return result;
		}catch(NoResultException e){							
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}
	
	public Registrant searchRegistrant(String user){
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			Registrant registrant = em.createNamedQuery("Registrant.searchByUsername",Registrant.class)
				.setParameter("username", user)
				.getSingleResult();			 		 
			 return registrant;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}

	public CvSampletype searchSampleType(String sampletypeidentifier) {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			CvSampletype cvSampletype = em.createNamedQuery("CvSampletype.searchBySampleType",CvSampletype.class)
				.setParameter("sampletypeidentifier", sampletypeidentifier)
				.getSingleResult();			 		 
			 return cvSampletype;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}
	
	public CvSamplingfeature searchSamplingfeatureByIdentifier(String identifier) {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			CvSamplingfeature cvSamplingfeature = em.createNamedQuery("CvSamplingfeature.searchByIdentifier",CvSamplingfeature.class)
				.setParameter("identifier", identifier)
				.getSingleResult();			 		 
			 return cvSamplingfeature;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}
	
	public CvRelatedIdentifiertype searchRelatedIdentifier(String relatedidentifiertype) {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			CvRelatedIdentifiertype cvRelatedIdentifiertype = em.createNamedQuery("CvRelatedIdentifiertype.searchByRelatedidentifiertype",CvRelatedIdentifiertype.class)
				.setParameter("relatedidentifiertype", relatedidentifiertype)
				.getSingleResult();			 		 
			 return cvRelatedIdentifiertype;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}
	
	public CvResourceRelationshiptype searchByRelationshipType(String relationshipType) {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			CvResourceRelationshiptype cvResourceRelationshiptype = em.createNamedQuery("CvResourceRelationshiptype.searchByRelationshipType",CvResourceRelationshiptype.class)
				.setParameter("relationshipType", relationshipType)
				.getSingleResult();			 		 
			 return cvResourceRelationshiptype;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}

	
	public CvSamplematerial searchByMaterialidentifier(String materialidentifier) {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{			
			CvSamplematerial cvSamplematerial = em.createNamedQuery("CvSamplematerial.searchByMaterialidentifier",CvSamplematerial.class)
				.setParameter("materialidentifier", materialidentifier)
				.getSingleResult();			 		 
			 return cvSamplematerial;
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw e;
		}finally{
			em.close();
		}
	}
}
