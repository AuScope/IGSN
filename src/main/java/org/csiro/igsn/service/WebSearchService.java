package org.csiro.igsn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.csiro.igsn.entity.postgres2_0.Sample;
import org.springframework.stereotype.Service;

@Service
public class WebSearchService {
	
	public List<Sample> search(String igsn, String type,
			
			Integer pageNumber, Integer pageSize) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();				
		CriteriaQuery<Sample> criteriaQuery = criteriaBuilder.createQuery(Sample.class);
		Root<Sample> from = criteriaQuery.from(Sample.class);
		
		
		from.fetch("cvSampletypes", JoinType.LEFT);
		from.fetch("samplecurations",JoinType.LEFT);
		from.fetch("cvSamplingmethod",JoinType.LEFT);
		from.fetch("registrant",JoinType.LEFT);
		from.fetch("statusByPhysicalsamplestatus",JoinType.LEFT);
		from.fetch("statusByRegistrationstatus",JoinType.LEFT);
		from.fetch("samplecurations",JoinType.LEFT);
		from.fetch("samplingfeatures",JoinType.LEFT);
		from.fetch("sampleCollectors",JoinType.LEFT);
		from.fetch("sampleresourceses",JoinType.LEFT);
		from.fetch("cvSamplematerials",JoinType.LEFT);
		
					
		List<Predicate> predicates =this.predicateBuilder(igsn,type, criteriaBuilder,from);
			
		CriteriaQuery<Sample> select = criteriaQuery.select(from).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		select = select.orderBy(criteriaBuilder.asc(from.get("sampleid")));
		
	
		TypedQuery<Sample> typedQuery = em.createQuery(select);
		
		if(pageNumber != null && pageSize != null){
		    typedQuery.setFirstResult((pageNumber - 1)*pageSize);
		    typedQuery.setMaxResults(pageSize);
		}
	    
		
	    List<Sample> result = typedQuery.getResultList();
		
		em.close();
		return result;
	}

	public Long searchSampleCount( String igsn,String sampleType) {
		
		EntityManager em = JPAEntityManager.createEntityManager();
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		 Root<Sample> from = countQuery.from(Sample.class);
		
		
		 List<Predicate> predicates =this.predicateBuilder(igsn,sampleType, criteriaBuilder,from);

		CriteriaQuery<Long> select = countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
	
		Long result = em.createQuery(select).getSingleResult();
		
		em.close();
		return result;
	}
	
	private List<Predicate> predicateBuilder(String igsn,String sampleType,CriteriaBuilder criteriaBuilder,Root<Sample> from){
				
		List<Predicate> predicates = new ArrayList<Predicate>();
						
		
		if (igsn != null && !igsn.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("igsn")),  "%"+ igsn.toUpperCase() +"%"));
		}
		
		if (sampleType != null && !sampleType.isEmpty()) {
			predicates.add(criteriaBuilder.equal(from.join("cvSampletypes").get("sampletypeidentifier"), sampleType));
		}
			
		
		return predicates;
	}

}
