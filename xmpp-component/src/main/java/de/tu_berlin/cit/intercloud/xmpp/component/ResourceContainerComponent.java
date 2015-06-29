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

package de.tu_berlin.cit.intercloud.xmpp.component;

import java.util.Iterator;

import org.apache.xmlbeans.XmlException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.core.component.AbstractComponent;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ.Type;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public abstract class ResourceContainerComponent extends AbstractComponent {

	protected final static Logger logger = LoggerFactory
			.getLogger(ResourceContainerComponent.class);

	/**
	 * The 'XWADL' namespace
	 * 
	 */
	public static final String NAMESPACE_REST_XWADL = "urn:xmpp:rest-xwadl";

	/**
	 * The 'REST XML' namespace
	 * 
	 */
	public static final String NAMESPACE_REST_XML = "urn:xmpp:xml-rest";

	private final ResourceContainer container;
	
	private String rootJID = null;

	private String exchangeJID = null;


	protected ResourceContainerComponent(ResourceContainer container) {
		this.container = container;
	}

	@Override
	protected String[] discoInfoFeatureNamespaces() {
		return (new String[] { NAMESPACE_REST_XWADL, NAMESPACE_REST_XML });
	}

	@Override
	protected String discoInfoIdentityCategory() {
		return ("automation");
	}

	@Override
	protected String discoInfoIdentityCategoryType() {
		return ("rest");
	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>get</tt> that
	 * could not be processed by the {@link AbstractComponent} implementation.
	 * 
	 * Note that, as any IQ stanza of type <tt>get</tt> must be replied to,
	 * returning <tt>null</tt> from this method equals returning an IQ error
	 * stanza of type 'feature-not-implemented' (this behavior can be disabled
	 * by setting the <tt>enforceIQResult</tt> argument in the constructor to
	 * <tt>false</tt>).
	 * 
	 * Note that if this method throws an Exception, an IQ stanza of type
	 * <tt>error</tt>, condition 'internal-server-error' will be returned to the
	 * sender of the original request.
	 * 
	 * The default implementation of this method returns <tt>null</tt>. It is
	 * expected that most child classes will override this method.
	 * 
	 * @param iq
	 *            The IQ request stanza of type <tt>get</tt> that was received
	 *            by this component.
	 * @return the response the to request stanza, or <tt>null</tt> to indicate
	 *         'feature-not-available'.
	 */
	@Override
	protected IQ handleIQGet(IQ iq) throws Exception {
		logger.info("the following iq get stanza has been received:" +
				iq.toString());
		Element child = iq.getChildElement();
		String path = child.attribute("path").getValue();
		ResourceTypeDocument xwadl = this.container.getXWADL(path);
		Document doc = DocumentHelper.parseText(xwadl.toString());
		IQ response = IQ.createResultIQ(iq);
		response.setChildElement(doc.getRootElement());
		logger.info("the following iq result stanza will be send:" +
				response.toString());
		return response;
	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>set</tt> that
	 * could not be processed by the {@link AbstractComponent} implementation.
	 * 
	 * Note that, as any IQ stanza of type <tt>set</tt> must be replied to,
	 * returning <tt>null</tt> from this method equals returning an IQ error
	 * stanza of type 'feature-not-implemented' {this behavior can be disabled
	 * by setting the <tt>enforceIQResult</tt> argument in the constructor to
	 * <tt>false</tt>).
	 * 
	 * Note that if this method throws an Exception, an IQ stanza of type
	 * <tt>error</tt>, condition 'internal-server-error' will be returned to the
	 * sender of the original request.
	 * 
	 * The default implementation of this method returns <tt>null</tt>. It is
	 * expected that most child classes will override this method.
	 * 
	 * @param iq
	 *            The IQ request stanza of type <tt>set</tt> that was received
	 *            by this component.
	 * @return the response the to request stanza, or <tt>null</tt> to indicate
	 *         'feature-not-available'.
	 */
	@Override
	protected IQ handleIQSet(IQ iq) throws Exception {
		logger.info("the following iq set stanza has been received:" +
				iq.toString());
		Element child = iq.getChildElement();
		ResourceDocument xmlRequest = ResourceDocument.Factory.parse(child
				.asXML());
		ResourceDocument xmlResponse = this.container.execute(xmlRequest);
		Document doc = DocumentHelper.parseText(xmlResponse.toString());
		IQ response = IQ.createResultIQ(iq);
		response.setChildElement(doc.getRootElement());
		logger.info("the following iq result stanza will be send:" +
				response.toString());
		return response;
	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>result</tt>
	 * that are received by the component. If you do not override this method,
	 * the stanzas are ignored.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>result</tt> that was received by
	 *            this component.
	 */
	@Override
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
	}

	protected void rootDiscovered() {
		// Doesn't do anything. Override this method to process IQ stanzas.
		logger.info("root discovery successfully completed!");
	}

	protected void exchangeDiscovered() {
		// Doesn't do anything. Override this method to process IQ stanzas.
		logger.info("exchange discovery successfully completed!");
	}

	protected abstract void handleRestXWADL(ResourceTypeDocument parse);

	protected abstract void handleRestXML(ResourceDocument parse);

	/**
	 * Override this method to handle the IQ stanzas of type <tt>error</tt> that
	 * are received by the component. If you do not override this method, the
	 * stanzas are ignored.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>error</tt> that was received by this
	 *            component.
	 */
	// @Override
	// protected void handleIQError(IQ iq) {
	// Doesn't do anything. Override this method to process IQ error
	// stanzas.
	// log.info("(serving component '{}') IQ stanza "
	// + "of type <tt>error</tt> received: ", getName(), iq.toXML());
	// }

	public void discoverIntercloudServices(String domain) {
		// discover services
		IQ discoIQ = new IQ(Type.get);
		logger.info("Start discovering domain: " + domain);
		discoIQ.setTo(domain);
		discoIQ.setFrom(getJID());
		discoIQ.setChildElement("query", NAMESPACE_DISCO_ITEMS);
		logger.info(discoIQ.toXML());
		// the response have to be caught in handleIQResult
		send(discoIQ);
	}

	public void discoverIntercloudFeatures(String jid) {
		// discover services
		IQ discoIQ = new IQ(Type.get);
		logger.info("Start discovering features of: " + jid);
		discoIQ.setTo(jid);
		discoIQ.setFrom(getJID());
		discoIQ.setChildElement("query", NAMESPACE_DISCO_INFO);
		logger.info(discoIQ.toXML());
		// the response have to be caught in handleIQResult
		send(discoIQ);
	}

	public String getRootJID() {
		return this.rootJID;
	}
	
	public String getExchangeJID() {
		return this.exchangeJID;
	}

}