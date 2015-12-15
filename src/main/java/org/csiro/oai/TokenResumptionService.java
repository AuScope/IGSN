package org.csiro.oai;


import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;


public class TokenResumptionService {
	
	CacheAccess<String, TokenResumption> cache;
	

	public TokenResumptionService(){
		cache = JCS.getInstance( "default" );
	}
	
	public String update(String key,TokenResumption token){
	
		cache.put(key, token);
		
		return key;
	}
	
	public String put(TokenResumption token){
		String key = UUID.randomUUID().toString();;
		token.setKey(key);		
		cache.put(key, token);
		
		return key;
	}
	
	public TokenResumption get(String key){
		return cache.get(key);
	}
	
	public XMLGregorianCalendar getExpiration(String key) throws DatatypeConfigurationException{

		GregorianCalendar c = new GregorianCalendar();
		c.add(GregorianCalendar.SECOND, (int)cache.getElementAttributes(key).getTimeToLiveSeconds());
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		
	}
}
