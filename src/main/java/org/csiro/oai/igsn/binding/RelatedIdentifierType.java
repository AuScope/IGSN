//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.26 at 09:11:18 AM AWST 
//


package org.csiro.oai.igsn.binding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for relatedIdentifierType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="relatedIdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="doi"/>
 *     &lt;enumeration value="handle"/>
 *     &lt;enumeration value="lsid"/>
 *     &lt;enumeration value="url"/>
 *     &lt;enumeration value="urn"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "relatedIdentifierType")
@XmlEnum
public enum RelatedIdentifierType {

    @XmlEnumValue("doi")
    DOI("doi"),
    @XmlEnumValue("handle")
    HANDLE("handle"),
    @XmlEnumValue("lsid")
    LSID("lsid"),
    @XmlEnumValue("url")
    URL("url"),
    @XmlEnumValue("urn")
    URN("urn");
    private final String value;

    RelatedIdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelatedIdentifierType fromValue(String v) {
        for (RelatedIdentifierType c: RelatedIdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
