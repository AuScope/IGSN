package org.csiro.igsn.entity.postgres2_0;

// Generated 27/10/2015 10:58:13 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;


/**
 * Samplingfeatures generated by hbm2java
 */
@Entity
@Table(name = "samplingfeatures")
public class Samplingfeatures implements java.io.Serializable {

	private int featureid;
	private CvSamplingfeature cvSamplingfeature;
	private String featurename;
	private Geometry featuregeom;
	private String featuresrs;
	private String elevation;
	private String verticaldatum;
	private String featurelocality;
	private String elevationUnits;
	private Set<Sampledfeatures> sampledfeatures = new HashSet<Sampledfeatures>(0);

	public Samplingfeatures() {
	}

	public Samplingfeatures(String featurename) {
		
		this.featurename = featurename;
	}

	public Samplingfeatures( CvSamplingfeature cvSamplingfeature,
			String featurename, Geometry featuregeom, String featuresrs,
			String elevation, String verticaldatum, String featurelocality,
			String elevationUnits,Set<Sampledfeatures> sampledfeatures) {
		
		this.cvSamplingfeature = cvSamplingfeature;
		this.featurename = featurename;
		this.featuregeom = featuregeom;
		this.featuresrs = featuresrs;
		this.elevation = elevation;
		this.verticaldatum = verticaldatum;
		this.featurelocality = featurelocality;
		this.elevationUnits = elevationUnits;
		this.sampledfeatures = sampledfeatures;
	}

	@Id
	@Column(name = "featureid", unique = true, nullable = false)
	@SequenceGenerator(name="samplingfeatures_featureid_seq",sequenceName="samplingfeatures_featureid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="samplingfeatures_featureid_seq")
	public int getFeatureid() {
		return this.featureid;
	}

	public void setFeatureid(int featureid) {
		this.featureid = featureid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "featuretype")
	public CvSamplingfeature getCvSamplingfeature() {
		return this.cvSamplingfeature;
	}

	public void setCvSamplingfeature(CvSamplingfeature cvSamplingfeature) {
		this.cvSamplingfeature = cvSamplingfeature;
	}

	@Column(name = "featurename", nullable = false, length = 100)
	public String getFeaturename() {
		return this.featurename;
	}

	public void setFeaturename(String featurename) {
		this.featurename = featurename;
	}
	
	@Column(name = "featuregeom")
	@Type(type="org.hibernate.spatial.GeometryType")
	@JsonIgnore
	public Geometry getFeaturegeom() {
		return this.featuregeom;
	}

	public void setFeaturegeom(Geometry featuregeom) {
		this.featuregeom = featuregeom;
	}

	@Column(name = "featuresrs", length = 20)
	public String getFeaturesrs() {
		return this.featuresrs;
	}

	public void setFeaturesrs(String featuresrs) {
		this.featuresrs = featuresrs;
	}

	@Column(name = "elevation", length = 30)
	public String getElevation() {
		return this.elevation;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	@Column(name = "verticaldatum", length = 20)
	public String getVerticaldatum() {
		return this.verticaldatum;
	}

	public void setVerticaldatum(String verticaldatum) {
		this.verticaldatum = verticaldatum;
	}

	@Column(name = "featurelocality", length = 150)
	public String getFeaturelocality() {
		return this.featurelocality;
	}

	public void setFeaturelocality(String featurelocality) {
		this.featurelocality = featurelocality;
	}
	
	@Column(name = "elevation_units", length = 100)
	public String getElevationUnits() {
		return this.elevationUnits;
	}

	public void setElevationUnits(String elevationUnits) {
		this.elevationUnits = elevationUnits;
	}

	@ManyToMany(fetch = FetchType.EAGER,cascade={CascadeType.ALL})
	@JoinTable(name = "sampling_sampled_mapping", joinColumns = { @JoinColumn(name = "samplingfeature_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "sampledfeature_id", nullable = false, updatable = false) })
	public Set<Sampledfeatures> getSampledfeatures() {
		return sampledfeatures;
	}

	public void setSampledfeatures(Set<Sampledfeatures> sampledfeatures) {
		this.sampledfeatures = sampledfeatures;
	}



}
