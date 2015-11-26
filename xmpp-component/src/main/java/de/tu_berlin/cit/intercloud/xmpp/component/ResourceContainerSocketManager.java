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

package de.tu_berlin.cit.intercloud.xmpp.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Exchanger;

import org.apache.xmlbeans.XmlException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.Message;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.Packet;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ.Type;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ResourceContainerSocketManager {

	private final static Logger logger = LoggerFactory
			.getLogger(ResourceContainerSocketManager.class);

	/**
	 * The Singleton instance
	 */
	private static ResourceContainerSocketManager instance;

	/**
	 * The ResourceContainerComponent instance
	 */
	private final ResourceContainerComponent component;
	
	/**
	 * Map of connected sockets
	 */
	private ConcurrentHashMap<String, ResourceContainerSocket> connections = new ConcurrentHashMap<String, ResourceContainerSocket>();
	
	private final Exchanger<IntercloudDiscoItems> discoItemExchanger = new Exchanger<IntercloudDiscoItems>();
	
	private final Exchanger<IntercloudDiscoFeatures> discoFeatureExchanger = new Exchanger<IntercloudDiscoFeatures>();
	
	/**
	 * Default constructor
	 * 
	 */
	private ResourceContainerSocketManager(ResourceContainerComponent component) {
		this.component = component;
	}
	
	/**
	 * This method builds the singleton instance. Only the ResourceContainerComponent is allowed 
	 * to call this method.
	 * 
	 * @param component The ResourceContainerComponent instance
	 * @return The singleton instance
	 */
	protected static ResourceContainerSocketManager buildInstance(ResourceContainerComponent component) {
		if (ResourceContainerSocketManager.instance == null) {
			ResourceContainerSocketManager.instance = new ResourceContainerSocketManager(component);
		} else {
			throw new RuntimeException("ResourceContainerSocketManager is already initialized!");
		}
		
		return ResourceContainerSocketManager.getInstance();
	}

	/**
	 * Return the singleton instance.
	 * 
	 * @return The Singleton instance
	 */
	public static synchronized ResourceContainerSocketManager getInstance() {
		if (ResourceContainerSocketManager.instance == null) {
			throw new RuntimeException("ResourceContainerSocketManager is not initialized!");
		}
		return ResourceContainerSocketManager.instance;
	}
	
	public ResourceContainerSocket createSocket(String jid) {
		return new ResourceContainerSocket(this, jid);
	}

	
	public synchronized IntercloudDiscoItems discoverIntercloudServices() {
		this.sendDiscoveryItems(this.component.getDomain());
		// wait for discovery result
		IntercloudDiscoItems discoItems = new IntercloudDiscoItems();
		try {
			discoItems = discoItemExchanger.exchange(discoItems);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return discoItems;
	}
	
	private void sendDiscoveryItems(String domain) {
		// discover services
		IQ discoIQ = new IQ(Type.get);
		logger.info("Start discovering domain: " + domain);
		discoIQ.setTo(domain);
		discoIQ.setFrom(this.component.getJID());
		discoIQ.setChildElement("query", ResourceContainerComponent.NAMESPACE_DISCO_ITEMS);
		logger.info(discoIQ.toXML());
		// the response have to be caught in handleIQResult
		this.sendPacket(discoIQ);
	}

	public synchronized IntercloudDiscoFeatures discoverIntercloudFeatures(String jid) {
		this.sendDiscoveryFeatures(jid);
		// wait for discovery result
		IntercloudDiscoFeatures discoFeatures = new IntercloudDiscoFeatures(jid);
		try {
			discoFeatures = discoFeatureExchanger.exchange(discoFeatures);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return discoFeatures;
	}
	
	private void sendDiscoveryFeatures(String jid) {
		// discover services
		IQ discoIQ = new IQ(Type.get);
		logger.info("Start discovering features of: " + jid);
		discoIQ.setTo(jid);
		discoIQ.setFrom(this.component.getJID());
		discoIQ.setChildElement("query", ResourceContainerComponent.NAMESPACE_DISCO_INFO);
		logger.info(discoIQ.toXML());
		// the response have to be caught in handleIQResult
		this.sendPacket(discoIQ);
	}
	

	/**
	 * This method handles the IQ stanzas of type <tt>result</tt>
	 * that are received by the component.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>result</tt> that was received by
	 *            this component.
	 */
	protected void handleIQResult(IQ iq) {
		logger.info("the following iq result stanza has been received:" +
				iq.toString());
		
		// IQ get (and set) stanza's MUST be replied to.
		final Element childElement = iq.getChildElement();
		String namespace = null;
		if (childElement != null) {
			namespace = childElement.getNamespaceURI();
		}
		if (namespace == null) {
			logger.info("(serving component '{}') Invalid XMPP "
					+ "- no child element or namespace in IQ "
					+ "request (packetId {})", this.component.getName(), iq.getID());
			// this isn't valid XMPP.
			return;
		}
		if (ResourceContainerComponent.NAMESPACE_DISCO_ITEMS.equals(namespace)) {
			logger.info("discovery item result.");
			@SuppressWarnings("rawtypes")
			Iterator iter = childElement.elementIterator();
			String rootJID = null;
			ArrayList<String> exchangeJIDs = new ArrayList<String>();
			ArrayList<String> gatewayJIDs = new ArrayList<String>();
			while (iter.hasNext()) {
				Element item = (Element) iter.next();
				// filter
				if(item.attributeValue("name").equals(ServiceNames.RootComponentName)) {
					rootJID = item.attributeValue("jid");
				} else if(item.attributeValue("name").equals(ServiceNames.ExchangeComponentName)) {
					exchangeJIDs.add(item.attributeValue("jid"));
				} else if(item.attributeValue("name").equals(ServiceNames.GatewayComponentName)) {
					gatewayJIDs.add(item.attributeValue("jid"));
				}
			}
			IntercloudDiscoItems discoItems = new IntercloudDiscoItems(rootJID, exchangeJIDs, gatewayJIDs);
			try {
				discoItems = discoItemExchanger.exchange(discoItems);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (ResourceContainerComponent.NAMESPACE_DISCO_INFO.equals(namespace)) {
			logger.info("discovery info result.");
			@SuppressWarnings("rawtypes")
			Iterator iter = childElement.elementIterator();
			ArrayList<String> features = new ArrayList<String>();
			while (iter.hasNext()) {
				Element feature = (Element) iter.next();
				if(feature.getName().equals("feature")) {
					features.add(feature.attributeValue("var"));
				}
			}
			IntercloudDiscoFeatures discoFeatures = new IntercloudDiscoFeatures(iq.getFrom().toBareJID(), features);
			try {
				discoFeatures = discoFeatureExchanger.exchange(discoFeatures);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (ResourceContainerComponent.NAMESPACE_REST_XWADL.equals(namespace)) {
			logger.info("received xwadl iq.");
			try {
				handleRestXWADL(iq.getID(), ResourceTypeDocument.Factory.parse(childElement
						.asXML()));
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (ResourceContainerComponent.NAMESPACE_REST_XML.equals(namespace)) {
			logger.info("received rest xml iq.");
			try {
				handleRestXML(iq.getID(), ResourceDocument.Factory.parse(childElement
						.asXML()));
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void handleRestXWADL(String key, ResourceTypeDocument parse) {
		logger.info("handleRestXWADL");
		ResourceContainerSocket resourceContainerSocket = this.connections.get(key);
		// remove key
		this.connections.remove(key);
		if(resourceContainerSocket == null) {
			throw new RuntimeException("unable to find resource for key");
		}
		
		try {
			resourceContainerSocket.xwadlExchanger.exchange(parse);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void handleRestXML(String key, ResourceDocument parse) {
		logger.info("handleRestXML");
		ResourceContainerSocket resourceContainerSocket = this.connections.get(key);
		// remove key
		this.connections.remove(key);
		if(resourceContainerSocket == null) {
			throw new RuntimeException("unable to find resource for key");
		}
		
		try {
			resourceContainerSocket.restXmlExchanger.exchange(parse);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method handles the IQ stanzas of type <tt>error</tt> that
	 * are received by the component.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>error</tt> that was received by this
	 *            component.
	 */
	protected void handleIQError(IQ iq) {
		logger.info("the following iq error stanza has been received:" +
				iq.toString());
	}

	protected void sendMessage(Message m) {
		m.setFrom(this.component.getJID());
		m.setType(Message.Type.normal);
		this.sendPacket(m);
	}

	protected void sendIQ(IQ iq, ResourceContainerSocket resourceContainerSocket) {
		iq.setFrom(this.component.getJID());
		// register socket
		this.connections.put(iq.getID(), resourceContainerSocket);
		// send iq
		this.sendPacket(iq);
	}

	protected void sendPacket(Packet packet) {
		this.component.sendPacket(packet);
	}
	
}
