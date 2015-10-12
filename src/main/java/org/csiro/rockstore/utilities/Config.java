package org.csiro.rockstore.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	public static Properties prop = new Properties();
	
	static {
		String filename = "config.properties";
		InputStream  input = Config.class.getClassLoader().getResourceAsStream(filename);
		try {
			prop.load(input);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public static String getLdapPassword() throws FileNotFoundException{
		if(prop.isEmpty()){
			throw new FileNotFoundException("Unable to locate property file");
		}else{
			return prop.getProperty("ldap.password");
		}
	}
	
	public static String getLdapUrl() throws FileNotFoundException{
		if(prop.isEmpty()){
			throw new FileNotFoundException("Unable to locate property file");
		}else{
			return prop.getProperty("ldap.url");
		}
	}
	
	public static String getUserDN() throws FileNotFoundException{
		if(prop.isEmpty()){
			throw new FileNotFoundException("Unable to locate property file");
		}else{
			return prop.getProperty("ldap.userdn");
		}
	}


}
