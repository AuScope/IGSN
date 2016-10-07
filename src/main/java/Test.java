import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.csiro.igsn.service.JPAEntityManager;

public class Test{
	
	
	public static void main(String [] args){
		EntityManager em = JPAEntityManager.createEntityManager();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX");
		try{
			Test t = new Test();

			Query q = em.createNativeQuery("SELECT a.metadataupdated, a.landingpage, a.igsn FROM Sample a where a.metadataupdated=false");
			List<Object[]> samples = q.getResultList();
			
			for(Object [] sample:samples){
				t.createRegistryXML(sample[2].toString(), (String)sample[1], sdf.format(new Date()), false, "submitted");
				
				EntityTransaction et = em.getTransaction();
				et.begin();
				em.createNativeQuery("update Sample set metadataupdated=true where igsn=?")
				.setParameter(1, sample[2].toString())
				.executeUpdate();
				et.commit();
			}
			
				
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			em.close();	
		}
	}
	

	
	
	public String createRegistryXML(String sampleNumber, String landingPage, String timeStamp, boolean testMode,
			String event) throws Exception {
			
		String IGSNPrefix = "10273/";
		String IGSN_REGISTRY_URL = "https://doidb.wdc-terra.org/igsn/";
		
		String metacontent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		metacontent += "<sample xmlns=\"http://igsn.org/schema/kernel-v.1.0\" "
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xsi:schemaLocation=\"http://igsn.org/schema/kernel-v.1.0 "
				+ "http://doidb.wdc-terra.org/igsn/schemas/igsn.org/schema/1.0/igsn.xsd\">";
		metacontent += "<sampleNumber identifierType=\"igsn\">" + IGSNPrefix + sampleNumber + "</sampleNumber>";
		metacontent += "<registrant><registrantName>CSIRO.CSIRO</registrantName></registrant>";
		metacontent += "<log><logElement event=\"" + event + "\" timeStamp=\"" + timeStamp + "\"/></log></sample>";
		
		
		ByteArrayOutputStream retbody = new ByteArrayOutputStream();
		try {
			
			
			int responseCode = httpRequest(IGSN_REGISTRY_URL + "metadata/" + IGSNPrefix + sampleNumber , metacontent.getBytes(), retbody, "POST");
			
			
			
			if(responseCode != 201){
				throw new Exception("Minting unsuccessful:" + retbody.toString());
			}
		} catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}
		return IGSNPrefix + sampleNumber;
	}
	
	
	
	
	
	private int httpRequest(String serviceurl, byte[] body, OutputStream retbody, String method)
			throws Exception {
		URL url;
		HttpsURLConnection con;
		boolean geterrorstream = false;
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		try {
			final SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			final SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

			url = new URL(serviceurl);
			con = (HttpsURLConnection) url.openConnection();
			((HttpsURLConnection) con).setSSLSocketFactory(sslSocketFactory);

			con.setRequestMethod(method);
		} catch (MalformedURLException e) {
			throw new Exception(e.getMessage());
		} catch (ProtocolException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} catch (GeneralSecurityException e) {
			throw new Exception(e.getMessage());
		}

		prepareTransmission(con, body);
		try {
			con.connect();
		} catch (IOException e) {
			geterrorstream = true;
			throw e;
		}

		try {
			int responsecode = con.getResponseCode();
			if (!geterrorstream && responsecode >= 200 && responsecode < 300) {
				IOUtils.copy(con.getInputStream(), retbody);
			} else {
				if (con.getErrorStream() != null)
					IOUtils.copy(con.getInputStream(), retbody);
			}
			return responsecode;
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private void prepareTransmission(HttpsURLConnection con, byte[] body) throws Exception {
		try {
			String igsnuser ="CSIRO.CSIRO:EpsVp5eZ";
			con.setRequestProperty("Authorization",
					"Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(igsnuser.getBytes()));
			con.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
			if (body.length > 0) {
				con.setDoOutput(true);
				OutputStream out = con.getOutputStream();
				out.write(body);
				out.close();
			}
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}

	}
}