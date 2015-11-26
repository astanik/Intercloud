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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.xmlbeans.XmlException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.core.component.AbstractComponent;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;
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
	private ConcurrentHashMap<ResourceContainerSocket, String> connections = new ConcurrentHashMap<ResourceContainerSocket, String>();
	
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
	
	public synchronized ResourceContainerSocket createRootSocket() {
		this.getRootJID();
		return new ResourceContainerSocket(this);
	}
	
	public synchronized ResourceContainerSocket createExchangeSocket() {
		return new ResourceContainerSocket(this);
	}
	
	public synchronized ResourceContainerSocket createGatewaySocket() {
		return new ResourceContainerSocket(this);
	}

	private ResourceContainerSocket createSocket() {
		return new ResourceContainerSocket(this);
	}

	protected void sendPacket(Packet packet) {
		this.component.sendPacket(packet);
	}
	
	protected void createConnection(ResourceContainerSocket resourceContainerSocket) {
		this.connections.put(resourceContainerSocket, "todo");
		// TODO Auto-generated method stub
		
	}

	protected void deleteConnection(ResourceContainerSocket resourceContainerSocket) {
		this.connections.remove(resourceContainerSocket);
		// TODO Auto-generated method stub
		
	}
	
	private String getRootJID() {
		//TODO
		return ""; //this.rootJID;
	}
	
	public String getExchangeJID() {
		return ""; //this.exchangeJID;
	}

	private void discoverIntercloudServices(String domain) {
		// discover services
		IQ discoIQ = new IQ(Type.get);
		logger.info("Start discovering domain: " + domain);
		discoIQ.setTo(domain);
		discoIQ.setFrom(this.component.getJID());
		discoIQ.setChildElement("query", ResourceContainerComponent.NAMESPACE_DISCO_ITEMS);
		logger.info(discoIQ.toXML());
		// the response have to be caught in handleIQResult
		sendPacket(discoIQ);
	}

	private void discoverIntercloudFeatures(String jid) {
		// discover services
		IQ discoIQ = new IQ(Type.get);
		logger.info("Start discovering features of: " + jid);
		discoIQ.setTo(jid);
		discoIQ.setFrom(this.component.getJID());
		discoIQ.setChildElement("query", ResourceContainerComponent.NAMESPACE_DISCO_INFO);
		logger.info(discoIQ.toXML());
		// the response have to be caught in handleIQResult
		sendPacket(discoIQ);
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
/*		
		// IQ get (and set) stanza's MUST be replied to.
		final Element childElement = iq.getChildElement();
		String namespace = null;
		if (childElement != null) {
			namespace = childElement.getNamespaceURI();
		}
		if (namespace == null) {
			logger.info("(serving component '{}') Invalid XMPP "
					+ "- no child element or namespace in IQ "
					+ "request (packetId {})", getName(), iq.getID());
			// this isn't valid XMPP.
			return;
		}
		if (NAMESPACE_DISCO_ITEMS.equals(namespace)) {
			logger.info("discovery item result.");
			@SuppressWarnings("rawtypes")
			Iterator iter = childElement.elementIterator();
			while (iter.hasNext()) {
				Element item = (Element) iter.next();
				// filter
				if(item.attributeValue("name").equals(ServiceNames.RootComponentName)) {
					this.rootJID = item.attributeValue("jid");
					System.out.println("Discovered root jid: " + this.rootJID);
					discoverIntercloudFeatures(this.rootJID);
				} else if(item.attributeValue("name").equals(ServiceNames.ExchangeComponentName)) {
					this.exchangeJID = item.attributeValue("jid");
					System.out.println("Discovered exchange jid: " + this.exchangeJID);
					discoverIntercloudFeatures(this.exchangeJID);
				}
			}
		} else if (NAMESPACE_DISCO_INFO.equals(namespace)) {
			logger.info("discovery info result.");
			@SuppressWarnings("rawtypes")
			Iterator iter = childElement.elementIterator();
			while (iter.hasNext()) {
				Element feature = (Element) iter.next();
				if(feature.getName().equals("feature")) {
					String fet = feature.attributeValue("var");
					System.out.println("Discovered feature: " + fet);
				}
			}
			if(this.rootJID != null) {
				if(iq.getFrom().equals(this.rootJID))
					rootDiscovered();
			}
			if(this.exchangeJID != null) {
				if(iq.getFrom().equals(this.exchangeJID))
					exchangeDiscovered();
			}
		} else if (NAMESPACE_REST_XWADL.equals(namespace)) {
			logger.info("received xwadl iq.");
			try {
				handleRestXWADL(ResourceTypeDocument.Factory.parse(childElement
						.asXML()));
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (NAMESPACE_REST_XML.equals(namespace)) {
			logger.info("received rest xml iq.");
			try {
				handleRestXML(ResourceDocument.Factory.parse(childElement
						.asXML()));
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
*/
	}

	protected void rootDiscovered() {
		// Doesn't do anything. Override this method to process IQ stanzas.
		logger.info("root discovery successfully completed!");
	}

	protected void exchangeDiscovered() {
		// Doesn't do anything. Override this method to process IQ stanzas.
		logger.info("exchange discovery successfully completed!");
	}

	protected void handleRestXWADL(ResourceTypeDocument parse) {
		// TODO Auto-generated method stub
		logger.info("handleRestXWADL");
	}

	protected void handleRestXML(ResourceDocument parse) {
		// TODO Auto-generated method stub
		logger.info("handleRestXML");
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

}
