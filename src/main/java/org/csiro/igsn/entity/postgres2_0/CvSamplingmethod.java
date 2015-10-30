package org.csiro.igsn.entity.postgres2_0;

// Generated 27/10/2015 10:58:13 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * CvSamplingmethod generated by hbm2java
 */
@Entity
@Table(name = "cv_samplingmethod")
public class CvSamplingmethod implements java.io.Serializable {

	private int methodid;
	private String methodidentifier;
	private String methoddescription;
	private Set<Sample> samples = new HashSet<Sample>(0);

	public CvSamplingmethod() {
	}

	public CvSamplingmethod(int methodid, String methodidentifier,
			String methoddescription) {
		this.methodid = methodid;
		this.methodidentifier = methodidentifier;
		this.methoddescription = methoddescription;
	}

	public CvSamplingmethod(int methodid, String methodidentifier,
			String methoddescription, Set<Sample> samples) {
		this.methodid = methodid;
		this.methodidentifier = methodidentifier;
		this.methoddescription = methoddescription;
		this.samples = samples;
	}

	@Id
	@Column(name = "methodid", unique = true, nullable = false)
	public int getMethodid() {
		return this.methodid;
	}

	public void setMethodid(int methodid) {
		this.methodid = methodid;
	}

	@Column(name = "methodidentifier", nullable = false)
	public String getMethodidentifier() {
		return this.methodidentifier;
	}

	public void setMethodidentifier(String methodidentifier) {
		this.methodidentifier = methodidentifier;
	}

	@Column(name = "methoddescription", nullable = false)
	public String getMethoddescription() {
		return this.methoddescription;
	}

	public void setMethoddescription(String methoddescription) {
		this.methoddescription = methoddescription;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cvSamplingmethod")
	public Set<Sample> getSamples() {
		return this.samples;
	}

	public void setSamples(Set<Sample> samples) {
		this.samples = samples;
	}

}