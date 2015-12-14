package org.csiro.binding;

import org.csiro.igsn.entity.postgres2_0.Sample;

public interface IGSNJAXBInterface {

	public boolean supports(String metadataPrefix);
	
	public Object convert(Sample sample);
}
