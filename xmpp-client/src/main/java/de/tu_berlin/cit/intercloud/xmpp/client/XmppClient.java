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

package de.tu_berlin.cit.intercloud.xmpp.client;

import java.io.IOException;
import java.net.URISyntaxException;

import de.tu_berlin.cit.intercloud.util.configuration.ClientConfig;
import de.tu_berlin.cit.intercloud.util.exceptions.ConfigurationException;
import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQProvider;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQProvider;


/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class XmppClient {

	private static ClientConfig clientConfig = ClientConfig.getInstance();

	/**
     * Main
     *
     */
	public static void main(String [] args) throws ConfigurationException {
		// Create a connection configuration
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				  .setUsernameAndPassword(clientConfig.getUsername(), clientConfig.getPassword())
				  .setServiceName(clientConfig.getServiceName())
				  .setHost(clientConfig.getHost())
				  .setPort(clientConfig.getPort())
				  .setSecurityMode(SecurityMode.disabled)
				  .build();

		System.out.println("Server Domain: " + clientConfig.getServiceName());

		// Create a connection to the xmpp server.
		AbstractXMPPConnection connection = new XMPPTCPConnection(config);
		System.out.println("Connection established ");
		try {
			// connect
			connection.connect();
			System.out.println("Connection connected ");
			// login
			connection.login();
			System.out.println("Loged in ");
			// add provider
			ProviderManager.addIQProvider(XwadlIQ.ELEMENT, XwadlIQ.NAMESPACE, new XwadlIQProvider());
			ProviderManager.addIQProvider(RestIQ.ELEMENT, RestIQ.NAMESPACE, new RestIQProvider());
			
			// perform the test
			TestClient client = new TestClient(connection);
			System.out.println("Test Client created ");
			client.performTest();
			
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			connection.disconnect();
			System.out.println("Connection closed ");
		}
	}

}
