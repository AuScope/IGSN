package org.csiro.igsn.entity.postgres2_0;

// Generated 27/10/2015 10:58:13 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * CvRelatedIdentifiertype generated by hbm2java
 */
@Entity
@Table(name = "cv_resource_relationshiptype")
@NamedQueries({
	@NamedQuery(
			name="CvResourceRelationshiptype.searchByRelationshipType",
		    query="SELECT r FROM CvResourceRelationshiptype r where r.relationshipType = :relationshipType"
	)
})	
public class CvResourceRelationshiptype implements java.io.Serializable {

	private int id;
	private String relationshipType;


	public CvResourceRelationshiptype() {
	}

	public CvResourceRelationshiptype(int id, String relationshipType) {
		this.id = id;
		this.relationshipType = relationshipType;
	}

	

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setid(int id) {
		this.id = id;
	}

	@Column(name = "relationship_type", nullable = false, length = 20)
	public String getRelationshipType() {
		return this.relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}



}
