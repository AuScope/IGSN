package org.csiro.igsn.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@ImportResource(value = {"/WEB-INF/applicationContext.xml"})
@Configuration
public class AppConfig {
	
//	 /**
//	  * Useless unless remove  <mvc:annotation-driven /> 
//      * @return MarshallingHttpMessageConverter object which is responsible for
//      *         marshalling and unMarshalling process
//      */
//    @Bean(name = "marshallingHttpMessageConverter")
//    public MarshallingHttpMessageConverter getMarshallingHttpMessageConverter() {
// 
//    	  Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
//    	  jaxb2Marshaller.setPackagesToScan("org.csiro.igsn.bindings.*");
//    	  Map<String,Object> map = new HashMap<String,Object>();
//    	  map.put("jaxb.formatted.output", true);
//    	  map.put(Marshaller.JAXB_SCHEMA_LOCATION, "http://igsn.org/schema/kernel-v.1.0 https://igsn.csiro.au/schemas/2.0/igsn-csiro-v2.0.xsd");
//    	  jaxb2Marshaller.setMarshallerProperties(map);
//    	  try{
//    		  jaxb2Marshaller.setSchema(new UrlResource("https://igsn.csiro.au/schemas/2.0/igsn-csiro-v2.0.xsd"));
//    	  }catch(MalformedURLException e){
//    		  e.printStackTrace();
//    		  return null;
//    	  }
//    	  
//    	
//        MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter();
//        marshallingHttpMessageConverter.setMarshaller(jaxb2Marshaller);
//        marshallingHttpMessageConverter.setUnmarshaller(jaxb2Marshaller);
//        return marshallingHttpMessageConverter;
//    }
 
   
	
   
//	public Jaxb2Marshaller getJaxb2Marshaller() {
//	  Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
//	  jaxb2Marshaller.setPackagesToScan("org.csiro.igsn.bindings.*");
//	  Map<String,Object> map = new HashMap<String,Object>();
//	  map.put("jaxb.formatted.output", true);
//	  map.put(Marshaller.JAXB_SCHEMA_LOCATION, "http://igsn.org/schema/kernel-v.1.0 https://igsn.csiro.au/schemas/2.0/igsn-csiro-v2.0.xsd");
//	  jaxb2Marshaller.setMarshallerProperties(map);
//	  try{
//		  jaxb2Marshaller.setSchema(new UrlResource("https://igsn.csiro.au/schemas/2.0/igsn-csiro-v2.0.xsd"));
//	  }catch(MalformedURLException e){
//		  e.printStackTrace();
//		  return null;
//	  }
//      return jaxb2Marshaller;
//	}
}
