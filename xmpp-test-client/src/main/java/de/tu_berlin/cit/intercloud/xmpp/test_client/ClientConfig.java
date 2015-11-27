package de.tu_berlin.cit.intercloud.xmpp.test_client;

import de.tu_berlin.cit.intercloud.util.exceptions.ConfigurationException;

import java.util.Properties;

public class ClientConfig extends ConfigHelper {
    private final static String FILE_NAME = "client";

    private final static String PROP_USERNAME = "username";
    private final static String PROP_PASSWORD = "password";
    private final static String PROP_HOST = "host";
    private final static String PROP_SERVICE_NAME = "serviceName";
    private final static String PROP_PORT = "port";

    private final static String SENSOR_URI = "sensorUri";
    private final static String SUBJECT_URI = "subjectUri";

    private static ClientConfig instance;

    private ClientConfig() {
        super(FILE_NAME);
    }

    public static synchronized ClientConfig getInstance() {
        if (null == instance) {
            instance = new ClientConfig();
        }
        return instance;
    }

    @Override
    protected void createExampleProperties(Properties prop) {
        prop.setProperty(PROP_USERNAME, "");
        prop.setProperty(PROP_PASSWORD, "");
        prop.setProperty(PROP_HOST, "");
        prop.setProperty(PROP_SERVICE_NAME, "");
        prop.setProperty(PROP_PORT, "5222");
        prop.setProperty(SENSOR_URI, "xmpp://gateway.cit.tu-berlin.de#/sensor/senX");
        prop.setProperty(SUBJECT_URI, "xmpp://gateway.cit.tu-berlin.de#/compute/vmX");
    }

    public String getUsername() {
        return getProperties().getProperty(PROP_USERNAME);
    }

    public String getPassword() {
        return getProperties().getProperty(PROP_PASSWORD);
    }

    public String getHost() {
        return getProperties().getProperty(PROP_HOST);
    }

    public String getServiceName() {
        return getProperties().getProperty(PROP_SERVICE_NAME);
    }

    public int getPort() throws ConfigurationException {
        String portStr = getProperties().getProperty(PROP_PORT);
        try {
            return Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("ClientConfig could not parse Integer of property " + PROP_PORT + ".");
        }
    }
    
    public String getSensorUri() {
        return getProperties().getProperty(SENSOR_URI);
    }

    public String getSubjectUri() {
        return getProperties().getProperty(SUBJECT_URI);
    }

}
