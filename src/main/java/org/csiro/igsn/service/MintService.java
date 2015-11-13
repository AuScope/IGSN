package org.csiro.igsn.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.csiro.igsn.web.controllers.IGSNMintCtrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MintService {
	final Logger log = Logger.getLogger(IGSNMintCtrl.class);
	
	private  final static Charset DEFAULT_ENCODING = Charset.forName("UTF8");
	
	private final String IGSN_TEST_PREFIX ="10273/TEST/";
	
	@Value("#{configProperties['IGSN_REGISTRY_URL']}")
	private String IGSN_REGISTRY_URL;

	@Value("#{configProperties['IGSN_PREFIX']}")
	private String IGSN_PREFIX;
	
	@Value("#{configProperties['IGSN_REGISTRY_USER']}")
	private String IGSN_USER;
	
	@Value("#{configProperties['IGSN_REGISTRY_PASSWORD']}")
	private String IGSN_PASSWORD;
	
	/**
	 * 
	 * @param sampleNumber
	 * @param landingPage
	 * @param timeStamp
	 * @param testMode
	 * @param event
	 * @return - the IGSN Number minted.
	 * @throws Exception
	 */
	public String createRegistryXML(String sampleNumber, String landingPage, String timeStamp, boolean testMode,
			String event) throws Exception {
			
		String IGSNPrefix = testMode?IGSN_TEST_PREFIX:IGSN_PREFIX;
		
		ByteArrayOutputStream retbody = null;
		try {
			
			String mintcontent = "igsn=" + IGSNPrefix + sampleNumber;
			mintcontent += "\n";
			mintcontent += "url=" + landingPage;
			retbody = new ByteArrayOutputStream();
			int responseCode = 0;
			if (testMode) {
				responseCode = httpRequest(IGSN_REGISTRY_URL + "igsn", mintcontent.getBytes(), retbody, "POST");
			} else {
				responseCode = httpRequest(IGSN_REGISTRY_URL + "igsn", mintcontent.getBytes(), retbody, "POST");
			}
			if(responseCode != 201){
				throw new Exception("Minting unsuccessful:" + retbody.toString());
			}
		} catch (Exception e) {
			log.info("Error: " + e.getMessage());
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
			String igsnuser = IGSN_USER + ":" + IGSN_PASSWORD;
			con.setRequestProperty("Authorization",
					"Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(igsnuser.getBytes()));
			con.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
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
