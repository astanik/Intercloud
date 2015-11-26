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

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.occi.core.OcciContainer;
import de.tu_berlin.cit.intercloud.xmpp.core.component.AbstractComponent;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.Packet;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
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
	
	private final ResourceContainerSocketManager socketManager;
	

	protected ResourceContainerComponent(ResourceContainer container) {
		this.container = container;
		this.socketManager = ResourceContainerSocketManager.buildInstance(this);
	}

	@Override
	protected String[] discoInfoFeatureNamespaces() {
		ArrayList<String> features = new ArrayList<String>();
		features.add(NAMESPACE_REST_XWADL);
		features.add(NAMESPACE_REST_XML);
		if(this.container instanceof OcciContainer) {
			features.addAll(((OcciContainer)this.container).getSupportedTypes());
		}
		return features.toArray(new String[0]);
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
		
		this.socketManager.handleIQResult(iq);
	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>error</tt> that
	 * are received by the component. If you do not override this method, the
	 * stanzas are ignored.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>error</tt> that was received by this
	 *            component.
	 */
	@Override
	protected void handleIQError(IQ iq) {
		logger.info("the following iq error stanza has been received:" +
				iq.toString());
		
		this.socketManager.handleIQError(iq);
	}

	/**
	 * Helper method for socket manager to send packets.
	 * 
	 * @param packet
	 *            The packet to send.
	 */
	protected void sendPacket(Packet packet) {
		send(packet);
	}

}