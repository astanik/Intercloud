package de.tu_berlin.cit.intercloud.xmpp.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;

import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQProvider;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQProvider;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;


public class XmppClient {

	private static String hostURL = "cit-mac1.cit.tu-berlin.de";

	private static String baseURL = "intercloud.cit.tu-berlin.de";

	/**
     * Main
     *
     */
	public static void main(String [] args) {
		// Create a connection configuration
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				  .setUsernameAndPassword("alex", "alex")
				  .setServiceName(baseURL)
				  .setHost(hostURL)
				  .setPort(5222)
				  .setSecurityMode(SecurityMode.disabled)
				  .build();

		System.out.println("Server Domain: " + baseURL);

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
			
			// discover root services
			// Obtain the ServiceDiscoveryManager associated with my XMPPConnection
			ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
			// Get the items of a given XMPP entity
			// This gets the items associated with online catalog service
			DiscoverItems discoItems = discoManager.discoverItems(baseURL);
			// Get the discovered items of the queried XMPP entity
			List<Item> items = discoItems.getItems();
			// Create list of root server
			List<XmppURI> roots = new ArrayList<XmppURI>();
			for(int i=0; i < items.size(); i++) {
				Item item = items.get(i);
				System.out.println(item.getEntityID());
				if(ServiceNames.RootComponentName.equals(item.getName()))
					roots.add(new XmppURI(item.getEntityID()));
			}

			// discover root features
			for(int i=0; i < roots.size(); i++) {
				// Get the information of a given XMPP entity
				System.out.println("Discover root: " + roots.get(i).getJID());
				// This gets the information of a root component
				DiscoverInfo discoInfo = discoManager.discoverInfo(roots.get(i).getJID());
				// Get the discovered identities of the remote XMPP entity
				List<Identity> identities = discoInfo.getIdentities();
				// Display the identities of the remote XMPP entity
				for(int k=0; k < identities.size(); k++) {
					Identity identity = identities.get(k);
					System.out.println(identity.getName());
					System.out.println(identity.getType());
					System.out.println(identity.getCategory());
				}
				// Check if root supports rest
				if(discoInfo.containsFeature("urn:xmpp:rest:xwadl"))
					System.out.println("XWADL is supported");
				else
					throw new SmackException("XWADL is not supported");

				if(discoInfo.containsFeature("urn:xmpp:rest:xml"))
					System.out.println("REST is supported");
				else
					throw new SmackException("REST is not supported");
			}
			// perform the test
//			TestClient client = new TestClient(connection);
//			System.out.println("Test Client created ");
//			client.performTest();
			
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
//		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
	}

}
