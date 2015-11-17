//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.17 at 09:26:47 AM AWST 
//


package org.csiro.igsn.bindings.allocation2_0;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nilReasonType.
 * 
 
 * 
 */
@XmlType(name = "nilReasonType")
@XmlEnum
public enum NilReasonType {

    @XmlEnumValue("http://www.opengis.net/def/nil/OGC/0/inapplicable")
    INAPPLICABLE("inapplicable"),
    @XmlEnumValue("http://www.opengis.net/def/nil/OGC/0/missing")
    MISSING("missing"),
    @XmlEnumValue("http://www.opengis.net/def/nil/OGC/0/template")
    TEMPLATE("template"),
    @XmlEnumValue("http://www.opengis.net/def/nil/OGC/0/unknown")
    UNKNOWN("unknown"),
    @XmlEnumValue("http://www.opengis.net/def/nil/OGC/0/withheld")
    WITHHELD("withheld");
    private final String value;

    NilReasonType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NilReasonType fromValue(String v) {
        for (NilReasonType c: NilReasonType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    
    public static boolean match(String v) {
        for (NilReasonType c: NilReasonType.values()) {
            if (c.value.equals(v)) {
                return true;
            }
        }
        return false;
        
    }

}
