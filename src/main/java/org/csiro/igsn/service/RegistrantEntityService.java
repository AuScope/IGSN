package org.csiro.igsn.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.csiro.igsn.entity.postgres2_0.Allocator;
import org.csiro.igsn.entity.postgres2_0.Registrant;
import org.springframework.stereotype.Service;

@Service
public class RegistrantEntityService {

	public Allocator isAuthority(String name) {
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			Allocator result = em.createNamedQuery("Allocator.search",Allocator.class)
		    .setParameter("username", name)
		    .getSingleResult();
			return result;
		}catch(NoResultException e){
			return null;
		}finally{
			em.close();
		}
		
	}

	public List<?> getAllRegistrant() {		
		EntityManager em = JPAEntityManager.createEntityManager();
		try{
			List<Registrant> result = em.createNamedQuery("Registrant.getAllRegistrant",Registrant.class)		   
		    .getResultList();
			return result;
		}catch(NoResultException e){
			return null;
		}finally{
			em.close();
		}
	}
	
	
	public void persist(Registrant registrant){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(registrant);
		em.flush();
		em.getTransaction().commit();
	    em.close();
	}


}
