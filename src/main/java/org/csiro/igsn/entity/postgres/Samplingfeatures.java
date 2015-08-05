package org.csiro.igsn.entity.postgres;

// Generated 04/08/2015 2:17:37 PM by Hibernate Tools 4.3.1

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Samplingfeatures generated by hbm2java
 */
@Entity
@Table(name = "samplingfeatures")
public class Samplingfeatures implements java.io.Serializable {

	private int featureId;
	private CvSamplingfeature cvSamplingfeature;
	private CvSpatialreferences cvSpatialreferences;
	private String featureName;
	private Point featureLocalXy;
	private Point featureLatlon;
	private Point featureLatlonEnd;
	private String featureLocality;

	public Samplingfeatures() {
	}

	public Samplingfeatures(String featureName) {		
		this.featureName = featureName;
	}

	public Samplingfeatures(CvSamplingfeature cvSamplingfeature,
			CvSpatialreferences cvSpatialreferences, String featureName,
			Point featureLocalXy, Point featureLatlon,
			Point featureLatlonEnd, String featureLocality) {
		
		this.cvSamplingfeature = cvSamplingfeature;
		this.cvSpatialreferences = cvSpatialreferences;
		this.featureName = featureName;
		this.featureLocalXy = featureLocalXy;
		this.featureLatlon = featureLatlon;
		this.featureLatlonEnd = featureLatlonEnd;
		this.featureLocality = featureLocality;
	}

	@Id
	@Column(name = "feature_id", unique = true, nullable = false)
	@SequenceGenerator(name="samplingfeatures_feature_id_seq",sequenceName="samplingfeatures_feature_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="samplingfeatures_feature_id_seq")
	public int getFeatureId() {
		return this.featureId;
	}

	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feature_type")
	public CvSamplingfeature getCvSamplingfeature() {
		return this.cvSamplingfeature;
	}

	public void setCvSamplingfeature(CvSamplingfeature cvSamplingfeature) {
		this.cvSamplingfeature = cvSamplingfeature;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feature_srs")
	public CvSpatialreferences getCvSpatialreferences() {
		return this.cvSpatialreferences;
	}

	public void setCvSpatialreferences(CvSpatialreferences cvSpatialreferences) {
		this.cvSpatialreferences = cvSpatialreferences;
	}

	@Column(name = "feature_name", nullable = false, length = 100)
	public String getFeatureName() {
		return this.featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	@Column(name = "feature_local_xy")
	@Type(type="org.hibernate.spatial.GeometryType")
	public Point getFeatureLocalXy() {
		return this.featureLocalXy;
	}

	public void setFeatureLocalXy(Point featureLocalXy) {
		this.featureLocalXy = featureLocalXy;
	}

	@Column(name = "feature_latlon")
	@Type(type="org.hibernate.spatial.GeometryType")
	public Point getFeatureLatlon() {
		return this.featureLatlon;
	}

	public void setFeatureLatlon(Point featureLatlon) {
		this.featureLatlon = featureLatlon;
	}

	@Column(name = "feature_latlon_end")
	@Type(type="org.hibernate.spatial.GeometryType")
	public Point getFeatureLatlonEnd() {
		return this.featureLatlonEnd;
	}

	public void setFeatureLatlonEnd(Point featureLatlonEnd) {
		this.featureLatlonEnd = featureLatlonEnd;
	}

	@Column(name = "feature_locality", length = 150)
	public String getFeatureLocality() {
		return this.featureLocality;
	}

	public void setFeatureLocality(String featureLocality) {
		this.featureLocality = featureLocality;
	}
	
	
	private static Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(),4326));
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (Exception e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }
	
	
	public static void main(String [] args){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.hibernate.igsn.jpa");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Samplingfeatures sf = new Samplingfeatures("sampleFeature1");
		Geometry geom = wktToGeometry("POINT (115.8589 31.9522)");
				
		sf.setFeatureLocalXy((Point) geom);
		sf.setFeatureLatlon((Point) geom);
		sf.setFeatureLatlonEnd((Point) geom);
		
		em.persist(sf);
        em.getTransaction().commit();
        em.close();
	}

}
