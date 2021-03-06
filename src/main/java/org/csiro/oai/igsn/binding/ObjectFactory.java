//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.26 at 09:11:18 AM AWST 
//


package org.csiro.oai.igsn.binding;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.csiro.igsn.bindings.allocation2_0 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SamplesSampleMaterialTypes_QNAME = new QName("http://igsn.org/schema/kernel-v.1.0", "materialTypes");
    private final static QName _SamplesSampleSamplingMethod_QNAME = new QName("http://igsn.org/schema/kernel-v.1.0", "samplingMethod");
    private final static QName _SamplesSampleSampleCollectors_QNAME = new QName("http://igsn.org/schema/kernel-v.1.0", "sampleCollectors");
    private final static QName _SamplesSampleSamplingTime_QNAME = new QName("http://igsn.org/schema/kernel-v.1.0", "samplingTime");
    private final static QName _SamplesSampleSamplingLocation_QNAME = new QName("http://igsn.org/schema/kernel-v.1.0", "samplingLocation");
    private final static QName _SamplesSampleSampleTypes_QNAME = new QName("http://igsn.org/schema/kernel-v.1.0", "sampleTypes");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.csiro.igsn.bindings.allocation2_0
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Samples }
     * 
     */
    public Samples createSamples() {
        return new Samples();
    }

    /**
     * Create an instance of {@link Samples.Sample }
     * 
     */
    public Samples.Sample createSamplesSample() {
        return new Samples.Sample();
    }

    /**
     * Create an instance of {@link Samples.Sample.RelatedResources }
     * 
     */
    public Samples.Sample.RelatedResources createSamplesSampleRelatedResources() {
        return new Samples.Sample.RelatedResources();
    }

    /**
     * Create an instance of {@link Samples.Sample.SampleCuration }
     * 
     */
    public Samples.Sample.SampleCuration createSamplesSampleSampleCuration() {
        return new Samples.Sample.SampleCuration();
    }

    /**
     * Create an instance of {@link Samples.Sample.SampleCuration.Curation }
     * 
     */
    public Samples.Sample.SampleCuration.Curation createSamplesSampleSampleCurationCuration() {
        return new Samples.Sample.SampleCuration.Curation();
    }

    /**
     * Create an instance of {@link Samples.Sample.SampleCuration.Curation.CurationTime }
     * 
     */
    public Samples.Sample.SampleCuration.Curation.CurationTime createSamplesSampleSampleCurationCurationCurationTime() {
        return new Samples.Sample.SampleCuration.Curation.CurationTime();
    }

    /**
     * Create an instance of {@link Samples.Sample.SampleCollectors }
     * 
     */
    public Samples.Sample.SampleCollectors createSamplesSampleSampleCollectors() {
        return new Samples.Sample.SampleCollectors();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingFeatures }
     * 
     */
    public Samples.Sample.SamplingFeatures createSamplesSampleSamplingFeatures() {
        return new Samples.Sample.SamplingFeatures();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingFeatures.SamplingFeature }
     * 
     */
    public Samples.Sample.SamplingFeatures.SamplingFeature createSamplesSampleSamplingFeaturesSamplingFeature() {
        return new Samples.Sample.SamplingFeatures.SamplingFeature();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures }
     * 
     */
    public Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures createSamplesSampleSamplingFeaturesSamplingFeatureSampledFeatures() {
        return new Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation }
     * 
     */
    public Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation createSamplesSampleSamplingFeaturesSamplingFeatureSamplingFeatureLocation() {
        return new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation();
    }

    /**
     * Create an instance of {@link Samples.Sample.SampleNumber }
     * 
     */
    public Samples.Sample.SampleNumber createSamplesSampleSampleNumber() {
        return new Samples.Sample.SampleNumber();
    }

    /**
     * Create an instance of {@link Samples.Sample.OtherNames }
     * 
     */
    public Samples.Sample.OtherNames createSamplesSampleOtherNames() {
        return new Samples.Sample.OtherNames();
    }

    /**
     * Create an instance of {@link Samples.Sample.IsPublic }
     * 
     */
    public Samples.Sample.IsPublic createSamplesSampleIsPublic() {
        return new Samples.Sample.IsPublic();
    }

    /**
     * Create an instance of {@link Samples.Sample.SampleTypes }
     * 
     */
    public Samples.Sample.SampleTypes createSamplesSampleSampleTypes() {
        return new Samples.Sample.SampleTypes();
    }

    /**
     * Create an instance of {@link Samples.Sample.MaterialTypes }
     * 
     */
    public Samples.Sample.MaterialTypes createSamplesSampleMaterialTypes() {
        return new Samples.Sample.MaterialTypes();
    }

    /**
     * Create an instance of {@link Samples.Sample.Classification }
     * 
     */
    public Samples.Sample.Classification createSamplesSampleClassification() {
        return new Samples.Sample.Classification();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingLocation }
     * 
     */
    public Samples.Sample.SamplingLocation createSamplesSampleSamplingLocation() {
        return new Samples.Sample.SamplingLocation();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingTime }
     * 
     */
    public Samples.Sample.SamplingTime createSamplesSampleSamplingTime() {
        return new Samples.Sample.SamplingTime();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingMethod }
     * 
     */
    public Samples.Sample.SamplingMethod createSamplesSampleSamplingMethod() {
        return new Samples.Sample.SamplingMethod();
    }

    /**
     * Create an instance of {@link Samples.Sample.LogElement }
     * 
     */
    public Samples.Sample.LogElement createSamplesSampleLogElement() {
        return new Samples.Sample.LogElement();
    }

    /**
     * Create an instance of {@link Samples.Sample.RelatedResources.RelatedResourceIdentifier }
     * 
     */
    public Samples.Sample.RelatedResources.RelatedResourceIdentifier createSamplesSampleRelatedResourcesRelatedResourceIdentifier() {
        return new Samples.Sample.RelatedResources.RelatedResourceIdentifier();
    }

    /**
     * Create an instance of {@link Samples.Sample.SampleCuration.Curation.CurationTime.TimePeriod }
     * 
     */
    public Samples.Sample.SampleCuration.Curation.CurationTime.TimePeriod createSamplesSampleSampleCurationCurationCurationTimeTimePeriod() {
        return new Samples.Sample.SampleCuration.Curation.CurationTime.TimePeriod();
    }

    /**
     * Create an instance of {@link Samples.Sample.SampleCollectors.Collector }
     * 
     */
    public Samples.Sample.SampleCollectors.Collector createSamplesSampleSampleCollectorsCollector() {
        return new Samples.Sample.SampleCollectors.Collector();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureName }
     * 
     */
    public Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureName createSamplesSampleSamplingFeaturesSamplingFeatureSamplingFeatureName() {
        return new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureName();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures.SampledFeature }
     * 
     */
    public Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures.SampledFeature createSamplesSampleSamplingFeaturesSamplingFeatureSampledFeaturesSampledFeature() {
        return new Samples.Sample.SamplingFeatures.SamplingFeature.SampledFeatures.SampledFeature();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt }
     * 
     */
    public Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt createSamplesSampleSamplingFeaturesSamplingFeatureSamplingFeatureLocationWkt() {
        return new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Wkt();
    }

    /**
     * Create an instance of {@link Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Elevation }
     * 
     */
    public Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Elevation createSamplesSampleSamplingFeaturesSamplingFeatureSamplingFeatureLocationElevation() {
        return new Samples.Sample.SamplingFeatures.SamplingFeature.SamplingFeatureLocation.Elevation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Samples.Sample.MaterialTypes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://igsn.org/schema/kernel-v.1.0", name = "materialTypes", scope = Samples.Sample.class)
    public JAXBElement<Samples.Sample.MaterialTypes> createSamplesSampleMaterialTypes(Samples.Sample.MaterialTypes value) {
        return new JAXBElement<Samples.Sample.MaterialTypes>(_SamplesSampleMaterialTypes_QNAME, Samples.Sample.MaterialTypes.class, Samples.Sample.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Samples.Sample.SamplingMethod }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://igsn.org/schema/kernel-v.1.0", name = "samplingMethod", scope = Samples.Sample.class)
    public JAXBElement<Samples.Sample.SamplingMethod> createSamplesSampleSamplingMethod(Samples.Sample.SamplingMethod value) {
        return new JAXBElement<Samples.Sample.SamplingMethod>(_SamplesSampleSamplingMethod_QNAME, Samples.Sample.SamplingMethod.class, Samples.Sample.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Samples.Sample.SampleCollectors }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://igsn.org/schema/kernel-v.1.0", name = "sampleCollectors", scope = Samples.Sample.class)
    public JAXBElement<Samples.Sample.SampleCollectors> createSamplesSampleSampleCollectors(Samples.Sample.SampleCollectors value) {
        return new JAXBElement<Samples.Sample.SampleCollectors>(_SamplesSampleSampleCollectors_QNAME, Samples.Sample.SampleCollectors.class, Samples.Sample.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Samples.Sample.SamplingTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://igsn.org/schema/kernel-v.1.0", name = "samplingTime", scope = Samples.Sample.class)
    public JAXBElement<Samples.Sample.SamplingTime> createSamplesSampleSamplingTime(Samples.Sample.SamplingTime value) {
        return new JAXBElement<Samples.Sample.SamplingTime>(_SamplesSampleSamplingTime_QNAME, Samples.Sample.SamplingTime.class, Samples.Sample.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Samples.Sample.SamplingLocation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://igsn.org/schema/kernel-v.1.0", name = "samplingLocation", scope = Samples.Sample.class)
    public JAXBElement<Samples.Sample.SamplingLocation> createSamplesSampleSamplingLocation(Samples.Sample.SamplingLocation value) {
        return new JAXBElement<Samples.Sample.SamplingLocation>(_SamplesSampleSamplingLocation_QNAME, Samples.Sample.SamplingLocation.class, Samples.Sample.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Samples.Sample.SampleTypes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://igsn.org/schema/kernel-v.1.0", name = "sampleTypes", scope = Samples.Sample.class)
    public JAXBElement<Samples.Sample.SampleTypes> createSamplesSampleSampleTypes(Samples.Sample.SampleTypes value) {
        return new JAXBElement<Samples.Sample.SampleTypes>(_SamplesSampleSampleTypes_QNAME, Samples.Sample.SampleTypes.class, Samples.Sample.class, value);
    }

}
