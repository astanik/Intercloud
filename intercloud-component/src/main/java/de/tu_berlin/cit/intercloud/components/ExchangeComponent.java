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

package de.tu_berlin.cit.intercloud.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.dom4j.Element;

import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.cep.ComplexEventProcessor;
import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;
import de.tu_berlin.cit.intercloud.xmpp.component.ResourceContainerComponent;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.Message;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ExchangeComponent extends ResourceContainerComponent {
	
	/**
	 * The 'eventlog' namespace
	 * 
	 */
	public static final String NAMESPACE_EVENT_LOG = "urn:xmpp:eventlog";
	
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
		String[] array = super.discoInfoFeatureNamespaces();
		@SuppressWarnings("unchecked")
		List<String> asList = Arrays.asList(array);
		ArrayList<String> features = new ArrayList<String>(asList);
		features.add(NAMESPACE_EVENT_LOG);
		return features.toArray(new String[0]);
	}

	protected void handleEventLogXML(LogDocument parse) {
		logger.info("handleEventLogXML");
		ComplexEventProcessor.getInstance().processEvent(parse);
	}

	/**
	 * This method handles the Message stanzas that are received by
	 * the component. It passes all stanzas of type logevent to the
	 * complex event processor.
	 * 
	 * @param message
	 *            The Message stanza that was received by this component.
	 */
	@Override
	protected void handleMessage(final Message message) {
		logger.info("the following message stanza has been received:" +
				message.toString());
		
		// check namespace.
		final List<Element> eventElements = message.getChildElements("log", NAMESPACE_EVENT_LOG);
		for(Element event : eventElements) {
			logger.info("received event log message.");
			try {
				handleEventLogXML(LogDocument.Factory.parse(event.asXML()));
			} catch (XmlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
//	public void postComponentStart() {
//		String domain = this.getDomain().replace("exchange.", "");
//	}



}
