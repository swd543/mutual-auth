package com.buga.boxes.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author Swapneel
 * To demonstrate mutual authentication on the client side, with self signed certificates.
 */
public class MutualAuthClient {
	
	private final static String url="https://localhost:8443/api/ping";
	
	public static void main(String[] args) throws ClientProtocolException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
		HttpClient client=new SSLClientBuilder(false).getClient();
		HttpGet get = new HttpGet(url);
		prettifyResponse(client.execute(get));
	}
	
	private static void prettifyResponse(HttpResponse response) throws UnsupportedOperationException, IOException {
		System.out.println("Got response...");
		System.out.println(response.getStatusLine());
		System.out.println(Arrays.toString(response.getAllHeaders()));
		System.out.println(getString(response.getEntity().getContent()));
	}
	
	public static String getString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();

		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))){
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}
		return sb.toString();
	}
}

/**
 * @author Swapneel
 * The HTTP Client builder class
 */
class SSLClientBuilder{
	private final String clientStoreUrl="../DataIngestPOC/cert/MyClient.p12";
	private final String clientPass="password";
	private final String trustStoreUrl="../DataIngestPOC/cert/MyClient.p12";
	private final String trustPass="password";

	private final HttpClient client;
	
	public SSLClientBuilder(boolean toVerifyHostname) throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {
		
		System.out.println("Attempting to build SSL Client...");
		/*
		 * Load the client keystore
		 */
		System.out.println("Loading client keystore...");
		KeyStore clientStore = KeyStore.getInstance("PKCS12");
		clientStore.load(new FileInputStream(clientStoreUrl), clientPass.toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(clientStore, clientPass.toCharArray());
		KeyManager[] kms = kmf.getKeyManagers();

		/*
		 * Load the client truststore
		 */
		System.out.println("Loading client truststore...");
		KeyStore trustStore = KeyStore.getInstance("PKCS12");
		trustStore.load(new FileInputStream(trustStoreUrl), trustPass.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);
		TrustManager[] tms = tmf.getTrustManagers();
		
		/*
		 * Build the SSL Context
		 */
		System.out.println("Building SSL context...");
		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kms, tms, new SecureRandom());
		
		/*
		 * Build the http client
		 * Note : If SSLHostNameverifier is set to NoopHostnameVerifier, the client will never
		 * check if the aliases in the pk12 file and the alias of the server match. This is a
		 * potential security breach.
		 * Remember to fix this before releasing into production.
		 */
		System.out.println("Building HTTP client...");
		HttpClientBuilder builder=HttpClientBuilder.create()
				.setSSLContext(sslContext);
		client=(toVerifyHostname) ? builder.build() : builder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
	}
	
	public HttpClient getClient() {
		return client;
	}
}