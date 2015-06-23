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

package de.tu_berlin.cit.intercloud.gateway;

import java.net.URISyntaxException;

import org.jivesoftware.whack.ExternalComponentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.occi.infrastructure.Compute;
import de.tu_berlin.cit.intercloud.xmpp.core.component.ComponentException;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;

public class GatewayApplication {

	private final static Logger logger = LoggerFactory.getLogger(GatewayApplication.class);
	
	private final static String xmppServer = "cit-mac1.cit.tu-berlin.de";
	
	private final static String xmppDomain = "intercloud.cit.tu-berlin.de";

	private final static String secretKey = "intercloud";

	private final static String subDomain = "gateway";

	/**
	 * Main
	 *
	 */
	public static void main(String[] args) {
		ExternalComponentManager mgr = new ExternalComponentManager(xmppServer, 5275);
		mgr.setSecretKey(subDomain, secretKey);
		try {
			XmppURI uri = new XmppURI(subDomain + "." + xmppDomain, "");
			ResourceContainer container = new ResourceContainer(uri);
			container.addResource(new Compute());
			GatewayComponent component = new GatewayComponent(container);
			mgr.addComponent(subDomain, component);
		} catch (ComponentException | URISyntaxException e) {
			logger.error(e.getMessage());
			System.exit(-1);
		}
		
		// Keep it alive
		while (true)
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
	}

}