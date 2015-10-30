package org.csiro.igsn.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.csiro.igsn.entity.postgres2_0.Prefix;
import org.csiro.igsn.entity.postgres2_0.Registrant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PrefixEntityService {

	
	public List<Prefix> listAllPrefix(){

		EntityManager em = JPAEntityManager.createEntityManager();
		List<Prefix> result = em.createNamedQuery("Prefix.listAll",Prefix.class)
	    //.setParameter("blah", blah)
	    .getResultList();
		em.close();		
		return result;

	}

	public ResponseEntity<String> registerPrefix(String usr, String string,
			String prefix) {
		
		ResponseEntity<String> response = null;
		
		if(search(prefix)!=null){
			response = new ResponseEntity<String>("Sub-namespace already exists!", HttpStatus.BAD_REQUEST);
		}else{
			
			EntityManager em = JPAEntityManager.createEntityManager();
			Registrant registrant = em.createNamedQuery("Registrant.searchByUsername",Registrant.class)
		    .setParameter("username", usr)
		    .getSingleResult();
			em.close();			 
			
			Set<Registrant> reg = new HashSet<Registrant>();
			reg.add(registrant);
			 
			Prefix p = new Prefix(prefix.toUpperCase(),new Date(),reg); 
			this.persist(p);
			
			response = new ResponseEntity<String>("Sub-namespace has been registered", HttpStatus.CREATED);
			
		}
		return response;
		
	}
	
	
	public void persist(Prefix rs){
		EntityManager em = JPAEntityManager.createEntityManager();
		em.getTransaction().begin();
		em.persist(rs);
		em.flush();
		em.getTransaction().commit();
	    em.close();	    
	}
	
	
	
	public Prefix search(String prefix){
		try{
			EntityManager em = JPAEntityManager.createEntityManager();
			Prefix result = em.createNamedQuery("Prefix.search",Prefix.class)
		    .setParameter("prefix", prefix)
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
