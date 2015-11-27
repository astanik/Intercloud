package de.tu_berlin.cit.intercloud.configuration;

import java.util.Properties;


public class OpenStackConfig extends ConfigHelper {

	private final static String fileName = "openstack";

	/**
	 * singleton instance
	 */
	private static OpenStackConfig instance;

	/**
	 * default constructor
	 * 
	 */
	private OpenStackConfig() {
		super(fileName);
	}

	public static synchronized OpenStackConfig getInstance() {
		if (OpenStackConfig.instance == null) {
			OpenStackConfig.instance = new OpenStackConfig();
		}
		return OpenStackConfig.instance;
	}

	@Override
	protected void createExampleProperties(Properties prop) {
		// set the example properties value
		prop.setProperty("endpoint", "http://xxx.xxx.xxx.xxx:5000/v2.0/");
		prop.setProperty("tenantName", "demo");
		prop.setProperty("userName", "demo");
		prop.setProperty("password", "devstack");
	}
	
	public String getEndpoint() {
		return this.getProperties().getProperty("endpoint");
	}
	
	public String getTenantName() {
		return this.getProperties().getProperty("tenantName");
	}
	
	public String getUserName() {
		return this.getProperties().getProperty("userName");
	}

	public String getPassword() {
		return this.getProperties().getProperty("password");
	}
}
