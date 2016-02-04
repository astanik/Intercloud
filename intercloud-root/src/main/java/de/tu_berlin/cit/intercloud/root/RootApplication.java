/**
 * Copyright 2010-2015 Complex and Distributed IT Systems, TU Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tu_berlin.cit.intercloud.root;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.components.RootComponent;
import de.tu_berlin.cit.intercloud.configuration.RootConfig;
import de.tu_berlin.cit.intercloud.occi.core.OcciContainer;
import de.tu_berlin.cit.intercloud.root.services.IaaSCatalog;
import de.tu_berlin.cit.intercloud.util.monitoring.CpuMeter;
import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.xmpp.core.ComponentException;
import de.tu_berlin.cit.rwx4j.xmpp.whack.ExternalComponentManager;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class RootApplication {

	private final static Logger logger = LoggerFactory.getLogger(RootApplication.class);

	private volatile boolean keepOn = true;
	
	private CpuMeter meter = null;

	/**
	 * Inner class for shutting down.
	 * 
	 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
	 *
	 */
	public class RunWhenShuttingDown extends Thread {
		public void run() {
			logger.info("Control-C caught. Shutting down...");
			keepOn = false;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Runs the application.
	 * 
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public void runProgram() throws InterruptedException, IOException {
		Runtime.getRuntime().addShutdownHook(new RunWhenShuttingDown());
		initialize();
		while (keepOn) {
			Thread.sleep(1000);
		}
		logger.info("Stopped running.");
		cleanup();
		logger.info("Shutted down.");
	}

	private void initialize() throws IOException {
		this.meter = new CpuMeter();
//		this.meter.start();
//		logger.info("CPU meter has been started.");
	}

	private void cleanup() {
//		this.meter.stop();
//		logger.info("CPU meter has been stopped.");
	}

	/**
	 * Main
	 *
	 */
	public static void main(String[] args) {
		RootConfig rootConf = RootConfig.getInstance();
		
		logger.info("Starting up...");
		ExternalComponentManager mgr = new ExternalComponentManager(rootConf.getXmppServer(),
				5275);
		mgr.setSecretKey(rootConf.getSubDomain(), rootConf.getSecretKey());
		try {
			XmppURI uri = new XmppURI(rootConf.getSubDomain() + "." + rootConf.getXmppDomain(), "");
			logger.info("Starting resource container: " + uri.toString());
			OcciContainer container = new OcciContainer(uri);
			container.addResource(new IaaSCatalog());
			RootComponent component = new RootComponent(container);
			mgr.addComponent(rootConf.getSubDomain(), component);
			logger.info("Container is up and running...");
			new RootApplication().runProgram();
		} catch (InterruptedException | IOException | ComponentException | URISyntaxException e) {
			logger.error(e.getMessage());
		}
	}

}