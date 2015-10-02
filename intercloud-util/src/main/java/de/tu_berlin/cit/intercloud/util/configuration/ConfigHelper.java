package de.tu_berlin.cit.intercloud.util.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConfigHelper {

	private final static Logger logger = LoggerFactory.getLogger(ConfigHelper.class);

	private final static String fileExtension = ".cfg";

	private final Properties prop = new Properties();

	/**
	 * constructor
	 * 
	 */
	protected ConfigHelper(String fileName) {
		logger.info("Reading config...");
		String homeDir = System.getProperty("user.home");
		File path = new File(homeDir + File.separatorChar + ".intercloud");
		if(!path.isDirectory()) {
			path.mkdir();
		}
		File file = new File(path.toString() + File.separatorChar + fileName
				+ fileExtension);
		if (!file.isFile()) {
			createExampleFile(file);
			throw new RuntimeException(
					"Config file does not exist, but example has been created!");
		} else {
			readProperties(file);
		}
		logger.info("Successful");
	}

	private void readProperties(File file) {
		InputStream input = null;

		try {
			input = new FileInputStream(file);

			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void createExampleFile(File file) {
		OutputStream output = null;

		try {
			output = new FileOutputStream(file);

			createExampleProperties(prop);

			// save properties to project folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	protected abstract void createExampleProperties(Properties prop);

	protected Properties getProperties() {
		return this.prop;
	}
	
}
