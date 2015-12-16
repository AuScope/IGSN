package org.csiro.binding;

import org.csiro.igsn.entity.postgres2_0.Sample;
import org.springframework.stereotype.Service;


public interface IGSNJAXBInterface {

	public boolean supports(String metadataPrefix);
	
	public String getMetadataPrefix();
	
	public String getNamespace();
	
	public String getSchemaLocation();
	
	public Object convert(Sample sample);
	
	public Class getXMLRootClass();
}
