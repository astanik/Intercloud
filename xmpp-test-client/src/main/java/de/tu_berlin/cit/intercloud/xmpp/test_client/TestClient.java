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

package de.tu_berlin.cit.intercloud.xmpp.test_client;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class TestClient {

	private static final String TEST_DOMAIN = ClientConfig.getInstance().getHost();

	private static final String SERVICE_PATH = "/iaas/compute/de";
	
	private XmppURI rootURI = null;
	
	private XmppURI exchangeURI = null;

	private final AbstractXMPPConnection connection;
	
	public TestClient(AbstractXMPPConnection connection) throws XMPPErrorException, URISyntaxException, SmackException, XmlException {
		this.connection = connection;
		
		discover();
	}
	
	private void discover() throws XMPPErrorException, URISyntaxException, SmackException, XmlException {
		// discover root services
		// Obtain the ServiceDiscoveryManager associated with my XMPPConnection
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
		// Get the items of a given XMPP entity
		// This gets the items associated with online catalog service
		DiscoverItems discoItems = discoManager.discoverItems(TEST_DOMAIN);
		// Get the discovered items of the queried XMPP entity
		List<Item> items = discoItems.getItems();
		// set exchange and root server
		for(Item item : items) {
			System.out.println(item.getEntityID());
			if(ServiceNames.RootComponentName.equals(item.getName()))
				rootURI = new XmppURI(item.getEntityID());
			else if(ServiceNames.ExchangeComponentName.equals(item.getName()))
				exchangeURI = new XmppURI(item.getEntityID());
		}

		// discover root features
		if(rootURI != null) {
			checkFeatures(discoManager, rootURI);
		}

		// discover exchange features
		if(exchangeURI != null) {
			checkFeatures(discoManager, exchangeURI);
		}

		XmppRestClient client = XmppRestClient.XmppRestClientBuilder.build(connection, new XmppURI(rootURI.toString(), SERVICE_PATH));
		Method method = client.getMethod(MethodType.GET, null, UriListText.MEDIA_TYPE);
		if (null != method) {
			XmppRestMethod xmppRestMethod = client.buildMethodInvocation(method);
			Representation representation = xmppRestMethod.invoke();
			System.out.println(representation.toString());
		}
	}

	private void checkFeatures(ServiceDiscoveryManager discoManager, XmppURI uri) throws SmackException, XMPPErrorException {
		// Get the information of a given XMPP entity
		System.out.println("Discover: " + uri.getJID());
		// This gets the information of the component
		DiscoverInfo discoInfo = discoManager.discoverInfo(uri.getJID());
		// Get the discovered identities of the remote XMPP entity
		List<Identity> identities = discoInfo.getIdentities();
		// Display the identities of the remote XMPP entity
		for(Identity identity : identities) {
			System.out.println(identity.getName());
			System.out.println(identity.getType());
			System.out.println(identity.getCategory());
		}
		// Check if component supports rest
		if(discoInfo.containsFeature(XwadlIQ.NAMESPACE))
			System.out.println("XWADL is supported");
		else
			throw new SmackException("XWADL is not supported");

		if(discoInfo.containsFeature(RestIQ.NAMESPACE))
			System.out.println("REST is supported");
		else
			throw new SmackException("REST is not supported");
	}

	public void performTest() throws FileNotFoundException, UnsupportedEncodingException, 
			URISyntaxException, XMPPErrorException, XmlException, SmackException {
		
		System.out.println("Start test ...");

		OcciXml slaTemplate = searchService();
		
		XmppURI offerEndpoint = createOffer(slaTemplate);
		
		XmppURI slaEndpoint = negotiateOffer(offerEndpoint);
		
		observeSLA(slaEndpoint);
/*		
				// delete vm
				deleteMeter.startTimer(i);
				client = XmppRestClient.XmppRestClientBuilder.build(connection, delUri);
				method = client.getMethod(MethodType.DELETE, null, null);
				if(method != null) {
					XmppRestMethod invocable = client.buildMethodInvocation(method);
				//	deleteMeter.startTimer(i);
					Representation message = invocable.invoke();
					deleteMeter.stopTimer(i);
//					System.out.println("===========Message:============");
					if(message != null)
						System.out.println(message);
				}
			}


			// create a new resources and continue
			{
				XmppURI uri = new XmppURI(testComponent, computePath);
				XmppRestClient client = XmppRestClient.XmppRestClientBuilder.build(connection, uri);
				Method method = client.getMethod(MethodType.POST, new OcciText(), new UriText());
				if(method != null) {
					List<Representation> rep = client.getRequestTemplates(method);
					if(rep.size() > 0) {
						if(rep.get(0) instanceof OcciText) {
							representation = (OcciText) rep.get(0);
							// create vm
							XmppRestMethod invocable = client.buildMethodInvocation(method);
							invocable.invoke(representation);
						}
					}
				}
			}
		}
*/
		System.out.println("Test was successful");
	}

	private OcciXml searchService() throws FileNotFoundException, UnsupportedEncodingException {
		// create file
		PrintWriter fileWriter = new PrintWriter("searchService.txt", "UTF-8");
		fileWriter.println("Start search at " + new Date());
		System.out.println("Start searchService");

		// search iterate over resources on server
/*
			XmppURI uri = new XmppURI(rootURI.getJID(), SERVICE_PATH);
			System.out.println("Create client for uri: " + uri);
			XmppRestClient client = XmppRestClient.XmppRestClientBuilder.build(connection, uri);
			Method method = client.getMethod(MethodType.GET, new OcciXml(), new OcciListXml());
			if(method != null) {
				// create search representation
				CategoryDocument catDoc = CategoryDocument.Factory.newInstance();
				CategoryType mixin = catDoc.addNewCategory().addNewMixin();
				OcciXml representation = new;
				
			//	flavorMeter.startTimer(i);
				List<Representation> rep = client.getRequestTemplates(method);
			//	flavorMeter.stopTimer(i);
				if(rep.size() > 0) {
					if(rep.get(0) instanceof OcciText) {
						representation = (OcciText) rep.get(0);
//						System.out.println("========Representation:========");
//						System.out.println(representation);
						// create vm
						XmppRestMethod invocable = client.buildMethodInvocation(method);
						createMeter.startTimer(i);
						Representation vmURI = invocable.invoke(representation);
						createMeter.stopTimer(i);
//						System.out.println("============VM URI:============");
//						System.out.println(vmURI);
						if(vmURI instanceof UriText) {
							delUri = new XmppURI(((UriText)vmURI).getUri());
						}
					}
				}
			}
*/
		// close file writer
		fileWriter.close();
		System.out.println("Finished searchService");
		return null;
	}

	private XmppURI createOffer(OcciXml slaTemplate) throws FileNotFoundException, UnsupportedEncodingException {
		// create file
		PrintWriter fileWriter = new PrintWriter("createOffer.txt", "UTF-8");
		fileWriter.println("Start offer creation at " + new Date());
		System.out.println("Start createOffer");


		// close file writer
		fileWriter.close();
		System.out.println("Finished createOffer");
		return null;
	}

	private XmppURI negotiateOffer(XmppURI offerEndpoint) throws FileNotFoundException, UnsupportedEncodingException {
		// create file
		PrintWriter fileWriter = new PrintWriter("negotiateOffer.txt", "UTF-8");
		fileWriter.println("Start offer negotiation at " + new Date());
		System.out.println("Start negotiateOffer");


		// close file writer
		fileWriter.close();
		System.out.println("Finished negotiateOffer");
		return null;
	}

	private void observeSLA(XmppURI slaEndpoint) throws FileNotFoundException, UnsupportedEncodingException {
		// create file
		PrintWriter fileWriter = new PrintWriter("observeSLA.txt", "UTF-8");
		fileWriter.println("Start SLA observation at " + new Date());
		System.out.println("Start observeSLA");


		// close file writer
		fileWriter.close();
		System.out.println("Finished observeSLA");
	}
		
}