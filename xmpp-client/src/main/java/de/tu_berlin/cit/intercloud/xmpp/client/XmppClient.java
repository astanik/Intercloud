package de.tu_berlin.cit.intercloud.xmpp.client;

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


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
				  .setServiceName("jabber.org")
				  .setHost(baseURL)
				  .setPort(5222)
				  .build();

		System.out.println("URL: " + baseURL);

		// Create a connection to the jabber.org server.
		AbstractXMPPConnection connection = new XMPPTCPConnection(config);
		try {
			// connect
			connection.connect();
			// login
			connection.login();
			
			// perform the test
			TestClient client = new TestClient(connection);
//			client.performTest();
			client.bla();
			
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
	}

}
