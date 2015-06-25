/**
 * Copyright (C) 2012-2015 TU Berlin. All rights reserved.
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

package de.tu_berlin.cit.intercloud.exchange;

import java.io.IOException;
import java.net.URISyntaxException;

import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.gateway.services.Compute;
import de.tu_berlin.cit.intercloud.util.monitoring.CpuMeter;
import de.tu_berlin.cit.intercloud.xmpp.core.component.ComponentException;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;

public class ExchangeApplication {

	private final static Logger logger = LoggerFactory.getLogger(ExchangeApplication.class);
	
	private final static String xmppServer = "cit-mac1.cit.tu-berlin.de";
	
	private final static String xmppDomain = "intercloud.cit.tu-berlin.de";

	private final static String secretKey = "intercloud";

	private final static String subDomain = "exchange";

	private volatile boolean keepOn = true;
	
	private CpuMeter meter = null;

	/**
	 * Inner class for shutting down.
	 * 
	 * @author stanik
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
		this.meter.start();
		logger.info("CPU meter has been started.");
	}

	private void cleanup() {
		this.meter.stop();
		logger.info("CPU meter has been stopped.");
	}

	/**
	 * Main
	 *
	 */
	public static void main(String[] args) {
		logger.info("Starting up...");
		ExternalComponentManager mgr = new ExternalComponentManager(xmppServer,
				5275);
		mgr.setSecretKey(subDomain, secretKey);
		try {
			XmppURI uri = new XmppURI(subDomain + "." + xmppDomain, "");
			logger.info("Starting resource container: " + uri.toString());
			ResourceContainer container = new ResourceContainer(uri);
			container.addResource(new Compute());
			ExchangeComponent component = new ExchangeComponent(container);
			mgr.addComponent(subDomain, component);
			logger.info("Container is up and running...");
			component.discoverRoot(xmppDomain);
			new ExchangeApplication().runProgram();
		} catch (InterruptedException | IOException | ComponentException | URISyntaxException e) {
			logger.error(e.getMessage());
		}
	}

}