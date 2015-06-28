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

package de.tu_berlin.cit.intercloud.exchange;

import java.util.Iterator;

import org.dom4j.Element;

import de.tu_berlin.cit.intercloud.occi.monitoring.MeterKind;
import de.tu_berlin.cit.intercloud.occi.sla.AgreementKind;
import de.tu_berlin.cit.intercloud.occi.sla.OfferKind;
import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.component.ResourceContainerComponent;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;

public class ExchangeComponent extends ResourceContainerComponent {
	
	public ExchangeComponent(ResourceContainer container) {
		super(container);
	}

	@Override
	public String getName() {
		return ServiceNames.ExchangeComponentName;
	}

	@Override
	public String getDescription() {
		return "This is the Intercloud Exchange service.";
	}

	@Override
	protected String[] discoInfoFeatureNamespaces() {
		return (new String[] { "urn:xmpp:rest:xwadl", "urn:xmpp:rest:xml",
				AgreementKind.AgreementSchema + AgreementKind.AgreementTerm,
				OfferKind.OfferSchema + OfferKind.OfferTerm,
				MeterKind.MeterSchema + MeterKind.MeterTerm});
	}

//	public void postComponentStart() {
//		String domain = this.getDomain().replace("exchange.", "");
//	}

	protected void handleIQResult(IQ iq) {
		logger.info("handleIQResult:" + iq.toXML());
		
		// IQ get (and set) stanza's MUST be replied to.
		final Element childElement = iq.getChildElement();
		String namespace = null;
		if (childElement != null) {
			namespace = childElement.getNamespaceURI();
		}
		if (namespace == null) {
			logger.debug("(serving component '{}') Invalid XMPP "
					+ "- no child element or namespace in IQ "
					+ "request (packetId {})", getName(), iq.getID());
			// this isn't valid XMPP.
			return;
		}
		if (NAMESPACE_DISCO_ITEMS.equals(namespace)) {
				logger.info("discovery result.");
				@SuppressWarnings("rawtypes")
				Iterator iter = childElement.elementIterator();
				while (iter.hasNext()) {
					Element item = (Element) iter.next();
					if(item.attributeValue("name").equals(ServiceNames.RootComponentName)) {
						System.out.println(item.attributeValue("jid"));
					}
				}
		} 
/* namespace of xwadl or rest xml
		else if (NAMESPACE_XMPP_PING.equals(namespace)) {
				log.trace("(serving component '{}') "
						+ "Calling #handlePing() (packetId {}).", getName(), iq
						.getID());
				return handlePing(iq);
			} else if (NAMESPACE_LAST_ACTIVITY.equals(namespace)) {
				log.trace("(serving component '{}') "
						+ "Calling #handleLastActivity() (packetId {}).", getName(), iq
						.getID());
				return handleLastActivity(iq);
			} else if (NAMESPACE_ENTITY_TIME.equals(namespace)) {
				log.trace("(serving component '{}') "
						+ "Calling #handleEntityTime() (packetId {}).", getName(), iq
						.getID());
				return handleEntityTime(iq);
			} else {
				return handleIQGet(iq);
			}
		}
*/
	}


}
