package de.tu_berlin.cit.intercloud.xmpp.client;

import java.io.IOException;
import java.net.URISyntaxException;

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


public class XmppClient {

	private static String baseURL = "cit-mac1.cit.tu-berlin.de";

	/**
     * Main
     *
     */
	public static void main(String [] args) {
		// Create a connection configuration
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				  .setUsernameAndPassword("alex", "alex")
				  .setServiceName("intercloud.cit.tu-berlin.de")
				  .setHost(baseURL)
				  .setPort(5222)
				  .setSecurityMode(SecurityMode.disabled)
				  .build();

		System.out.println("URL: " + baseURL);

		// Create a connection to the jabber.org server.
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
		}
	}

}
