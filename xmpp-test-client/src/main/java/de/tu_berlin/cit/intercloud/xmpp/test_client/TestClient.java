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
import de.tu_berlin.cit.intercloud.occi.core.incarnation.RepresentationBuilder;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceEvaluatorLink;
import de.tu_berlin.cit.intercloud.occi.sla.TimeWindowMetricMixin;
import de.tu_berlin.cit.intercloud.sla.mixins.AvailabilityMixin;
import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.cep.ComplexEventProcessor;
import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;
import de.tu_berlin.cit.intercloud.xmpp.cep.events.AvailabilityEvent;
import de.tu_berlin.cit.intercloud.xmpp.cep.events.CpuUtilizationEvent;
import de.tu_berlin.cit.intercloud.xmpp.cep.mixins.EventLogMixin;
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
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class TestClient {

	private static final String TEST_DOMAIN = ClientConfig.getInstance().getServiceName();

	private static final String SERVICE_PATH = "/agreement/testSLA";

	private final String sensorURI;

	private final String vmURI;
	
	private String rootURI = null;
	
	private String exchangeURI = null;

	private final AbstractXMPPConnection connection;
	
	public TestClient(AbstractXMPPConnection connection) throws XMPPErrorException, URISyntaxException, SmackException, XmlException {
		this.connection = connection;
		
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hostname = "gateway.cit.tu-berlin.de";
		}
		this.sensorURI = "xmpp://" + hostname + "#/sensor/senX";
		this.vmURI = "xmpp://" + hostname + "#/compute/vmX";
		
		discover();
	}
	
	private void discover() throws XMPPErrorException, URISyntaxException, SmackException, XmlException {
		System.out.println("discover root services");
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
				rootURI = item.getEntityID();
			else if(ServiceNames.ExchangeComponentName.equals(item.getName()))
				exchangeURI = item.getEntityID();
		}

		// discover root features
		if(rootURI != null) {
			checkFeatures(discoManager, rootURI);
		}

		// discover exchange features
		if(exchangeURI != null) {
			checkFeatures(discoManager, exchangeURI);
		}
	}

	private void checkFeatures(ServiceDiscoveryManager discoManager, String uri) throws SmackException, XMPPErrorException {
		// Get the information of a given XMPP entity
		System.out.println("Discover: " + uri);
		// This gets the information of the component
		DiscoverInfo discoInfo = discoManager.discoverInfo(uri);
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

	
	
	
	private class AvailabilityTimerTask extends TimerTask {
		
		public AvailabilityTimerTask() {
//			this.sensorURI = ClientConfig.getInstance().getSensorUri();
//			this.vmURI = ClientConfig.getInstance().getSubjectUri();
		}
	    @Override
	    public void run() {
	    	Random r = new Random();
	    	double availability = r.nextDouble() * 100;
	    	LogDocument event = AvailabilityEvent.build(sensorURI, vmURI, availability);
	    	// process event
	    	EventLogExtension message = new EventLogExtension(event);
//	    	Message m = new Message(exchangeURI, str1);
	//    	m.addExtension(extension);
	    	try {
				connection.sendStanza(message.toMessage(exchangeURI));
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	System.out.println("Event had been processed: " + event.toString());
	    }
	}
	
	private Timer timer = null;

	
	public void start() {
        TimerTask timerTask = new AvailabilityTimerTask();
        //running timer task as daemon thread
        this.timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
        System.out.println("TimerTask started");
	}

	public void stop() {
        this.timer.cancel();
        System.out.println("TimerTask cancelled");
	}
	
	private void createAvailabilityTerm() throws XMPPErrorException, XmlException, SmackException, URISyntaxException {
		XmppRestClient client = XmppRestClient.XmppRestClientBuilder.build(connection, new XmppURI(exchangeURI.toString(), SERVICE_PATH));
		Method method = client.getMethod(MethodType.PUT, OcciXml.MEDIA_TYPE, UriListText.MEDIA_TYPE);
		if (null != method) {
//			List<Representation> rep = client.getRequestTemplates(method);
//			if(rep.size() > 0) {
//				if(rep.get(0) instanceof OcciXml) {
//				Representation representation = (OcciXml) rep.get(0);
					Representation representation = createTestRepresentation();
					System.out.println("Request: " + representation.toString());
					// create vm
					XmppRestMethod invocable = client.buildMethodInvocation(method);
					representation = invocable.invoke(representation);
					System.out.println("Response: " + representation.toString());
//				}
//			}
		}
	}
	
	private OcciXml createTestRepresentation() {
		ServiceEvaluatorLink evaluator = new ServiceEvaluatorLink();
//		evaluator.setObject(ClientConfig.getInstance().getSensorUri());
//		evaluator.setSubject(ClientConfig.getInstance().getSubjectUri());
		evaluator.setObject(this.sensorURI);
		evaluator.setSubject(this.vmURI);
		evaluator.aggregationOperator = ServiceEvaluatorLink.AggregationOperator.avg;
		evaluator.relationalOperator = ServiceEvaluatorLink.RelationalOperator.GREATER_THAN_OR_EQUAL_TO;
		TimeWindowMetricMixin timeWindow = new TimeWindowMetricMixin();
		timeWindow.durationUnit = TimeWindowMetricMixin.TimeUnit.seconds;
		timeWindow.windowDuration = 5;
		EventLogMixin eventLog = new EventLogMixin();
		eventLog.eventID = AvailabilityEvent.AvailabilityStream;
		AvailabilityMixin availability = new AvailabilityMixin();
		availability.slo = new Double(50);
		
		// create link document
		LinkType link;
		try {
			link = RepresentationBuilder.buildLinkRepresentation(evaluator);
			link = RepresentationBuilder.appendLinkMixin(link, timeWindow);
			link = RepresentationBuilder.appendLinkMixin(link, eventLog);
			link = RepresentationBuilder.appendLinkMixin(link, availability);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			link = LinkType.Factory.newInstance();
		}
		// add the link representation
		OcciXml rep = new OcciXml();
		rep.addLink(link);
		return rep;
	}
	
	public void performTest() throws FileNotFoundException, UnsupportedEncodingException, 
			URISyntaxException, XMPPErrorException, XmlException, SmackException {
		
		System.out.println("Start test ...");
		
		this.createAvailabilityTerm();
		System.out.println("Availability Guarantee Term has been created.");
		
		// start measuring 
		this.start();
		System.out.println("CPU meter has been started.");
		try {
			// wait 10 seconds
			System.out.println("Press enter to exit!!!");
			System.in.read();
//			Thread.sleep(10 * 1000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stop();
		System.out.println("CPU meter has been stopped.");
		
		
		
		

//		OcciXml slaTemplate = searchService();
		
//		XmppURI offerEndpoint = createOffer(slaTemplate);
		
//		XmppURI slaEndpoint = negotiateOffer(offerEndpoint);
		
//		observeSLA(slaEndpoint);
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