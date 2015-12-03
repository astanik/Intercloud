package de.tu_berlin.cit.intercloud.configuration;

import java.util.Properties;


public class ExchangeConfig extends ConfigHelper {

	private final static String fileName = "exchange";

	/**
	 * singleton instance
	 */
	private static ExchangeConfig instance;

	/**
	 * default constructor
	 * 
	 */
	private ExchangeConfig() {
		super(fileName);
	}

	public static synchronized ExchangeConfig getInstance() {
		if (ExchangeConfig.instance == null) {
			ExchangeConfig.instance = new ExchangeConfig();
		}
		return ExchangeConfig.instance;
	}

	@Override
	protected void createExampleProperties(Properties prop) {
		// set the example properties value
		prop.setProperty("xmppServer", "server.example.org");
		prop.setProperty("xmppDomain", "intercloud.example.org");
		prop.setProperty("subDomain", "exchange");
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
