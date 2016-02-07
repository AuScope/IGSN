package org.csiro.igsn.entity.postgres2_0;

// Generated 27/10/2015 10:58:13 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * CvSamplematerial generated by hbm2java
 */
@Entity
@Table(name = "cv_samplematerial")
@NamedQueries({
	@NamedQuery(
			name="CvSamplematerial.searchByMaterialidentifier",
		    query="SELECT r FROM CvSamplematerial r where r.materialidentifier = :materialidentifier"
	)
})	
public class CvSamplematerial implements java.io.Serializable {

	private int materialid;
	private String materialidentifier;
	private String materialdesc;
	private Set<Sample> samples = new HashSet<Sample>(0);

	public CvSamplematerial() {
	}

	public CvSamplematerial(int materialid, String materialidentifier) {
		this.materialid = materialid;
		this.materialidentifier = materialidentifier;
	}

	public CvSamplematerial(int materialid, String materialidentifier,
			String materialdesc, Set<Sample> samples) {
		this.materialid = materialid;
		this.materialidentifier = materialidentifier;
		this.materialdesc = materialdesc;
		this.samples = samples;
	}

	@Id
	@Column(name = "materialid", unique = true, nullable = false)
	public int getMaterialid() {
		return this.materialid;
	}

	public void setMaterialid(int materialid) {
		this.materialid = materialid;
	}

	@Column(name = "materialidentifier", nullable = false, length = 250)
	public String getMaterialidentifier() {
		return this.materialidentifier;
	}

	public void setMaterialidentifier(String materialidentifier) {
		this.materialidentifier = materialidentifier;
	}

	@Column(name = "materialdesc", length = 45)
	public String getMaterialdesc() {
		return this.materialdesc;
	}

	public void setMaterialdesc(String materialdesc) {
		this.materialdesc = materialdesc;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "cvSamplematerials")
	@JsonIgnore
	public Set<Sample> getSamples() {
		return this.samples;
	}

	public void setSamples(Set<Sample> samples) {
		this.samples = samples;
	}

}
