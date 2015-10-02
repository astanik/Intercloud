package de.tu_berlin.cit.intercloud.util.configuration;

import java.util.Properties;


public class GatewayConfig extends ConfigHelper {

	private final static String fileName = "gateway";

	/**
	 * singleton instance
	 */
	private static GatewayConfig instance;

	/**
	 * default constructor
	 * 
	 */
	private GatewayConfig() {
		super(fileName);
	}

	public static synchronized GatewayConfig getInstance() {
		if (GatewayConfig.instance == null) {
			GatewayConfig.instance = new GatewayConfig();
		}
		return GatewayConfig.instance;
	}

	@Override
	protected void createExampleProperties(Properties prop) {
		// set the example properties value
		prop.setProperty("xmppServer", "server.example.org");
		prop.setProperty("xmppDomain", "intercloud.example.org");
		prop.setProperty("secretKey", "myKey");
	}
	
	public String getXmppServer() {
		return this.getProperties().getProperty("xmppServer");
	}
	
	public String getXmppDomain() {
		return this.getProperties().getProperty("xmppDomain");
	}
	
	public String getSecretKey() {
		return this.getProperties().getProperty("secretKey");
	}
}
