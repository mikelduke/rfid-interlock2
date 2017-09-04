package com.mikelduke.rfid.interlock2.makertracker;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import com.mikelduke.rfid.interlock2.AccessControlClient;
import com.mikelduke.rfid.interlock2.AccessInfo;
import com.mikelduke.rfid.interlock2.Configuration;

public class MakerTrackerClient implements AccessControlClient {
	private static final String CLAZZ = MakerTrackerClient.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	public static final String SERVER_URL = "server-url";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	private String serverUrl = "";
	private String toolid = "";
	private String authHeader = "";

	private Client client;

	@Override
	public void configure(Properties properties) {
		this.toolid = properties.getProperty(Configuration.TOOL_ID);
		this.serverUrl = properties.getProperty(SERVER_URL);

		String username = properties.getProperty(USERNAME);
		String password = properties.getProperty(PASSWORD);

		authHeader = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

		SSLContext ctx = null; //TODO Make the trust all config optional since its insecure
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		this.client = ClientBuilder.newBuilder()
				.hostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				})
				.sslContext(ctx)
				.build();
	}

	@Override
	public long getAccessTime(String rfid) {
		LOGGER.info("Requesting access with url: " + serverUrl + "/" + toolid + "/rfids/" + rfid);
		
		String accessTime = client
				.target(serverUrl + "/" + toolid + "/rfids/" + rfid)
				.request()
				.header("Authorization", "Basic " + authHeader)
				.get(String.class);
		
		return Long.parseLong(accessTime);
	}

	@Override
	public AccessInfo getAccessInfo() {
		MakerTrackerDTO access = client
				.target(serverUrl)
				.path(toolid)
				.path("rfids")
				.request()
				.header("Authorization", "Basic " + authHeader)
				.accept(MediaType.APPLICATION_JSON)
				.get(MakerTrackerDTO.class);

		AccessInfo info = AccessInfo.builder()
				.accessTimeMS(access.getAccessTimeMS())
				.toolId(access.getAssetId() + "")
				.rfidList(access.getRfidList())
				.build();
		return info;
	}

	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
