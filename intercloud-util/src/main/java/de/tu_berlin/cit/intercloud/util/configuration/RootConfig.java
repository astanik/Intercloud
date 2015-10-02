package de.tu_berlin.cit.intercloud.util.configuration;

import java.util.Properties;


public class RootConfig extends ConfigHelper {

	private final static String fileName = "root";

	/**
	 * singleton instance
	 */
	private static RootConfig instance;

	/**
	 * default constructor
	 * 
	 */
	private RootConfig() {
		super(fileName);
	}

	public static synchronized RootConfig getInstance() {
		if (RootConfig.instance == null) {
			RootConfig.instance = new RootConfig();
		}
		return RootConfig.instance;
	}

	@Override
	protected void createExampleProperties(Properties prop) {
		// set the example properties value
		prop.setProperty("xmppServer", "server.example.org");
		prop.setProperty("xmppDomain", "intercloud.example.org");
		prop.setProperty("subDomain", "root");
		prop.setProperty("secretKey", "myKey");
	}
	
	public String getXmppServer() {
		return this.getProperties().getProperty("xmppServer");
	}
	
	public String getXmppDomain() {
		return this.getProperties().getProperty("xmppDomain");
	}
	
	public String getSubDomain() {
		return this.getProperties().getProperty("subDomain");
	}
	
	public String getSecretKey() {
		return this.getProperties().getProperty("secretKey");
	}
}
