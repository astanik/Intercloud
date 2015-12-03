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

package de.tu_berlin.cit.intercloud.xmpp.cep;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.xml.sax.SAXException;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationEventTypeXMLDOM;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;
import de.tu_berlin.cit.intercloud.xmpp.cep.events.AvailabilityEvent;
import de.tu_berlin.cit.intercloud.xmpp.cep.events.CpuUtilizationEvent;
import de.tu_berlin.cit.intercloud.xmpp.cep.events.LogEvent;

/**
 * This class implements a singleton instance of a CEP.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ComplexEventProcessor {

	protected final static Logger logger = LoggerFactory.getLogger(ComplexEventProcessor.class);

	/**
	 * Esper provider instance
	 */
	private final EPServiceProvider epService;
	
	/**
	 * Event Log xsd name
	 */
	private static final String eventLogFile= "eventlog.xsd";
	
	/**
	 * Default constructor
	 * 
	 */
	private ComplexEventProcessor() {
		this.epService = this.configure();
	}
	
	/**
	 * This method configures the CEP.
	 * 
	 * @return It returns a well configured esper service provider instance.
	 */
	private EPServiceProvider configure() {
		// configure logging
		System.setProperty(DOMImplementationRegistry.PROPERTY,
				"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");
		
		// configure supported event ids
		Configuration config = new Configuration();
		config.addEventTypeAutoName(LogEvent.class.getPackage().getName());
		
		// create provider
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(config);

		// discover schema url
		URL schemaURL = ComplexEventProcessor.class.getClassLoader().getResource(eventLogFile);
		logger.info("xsd path is: " + schemaURL.toString());
		
		// configure default xml stream
		ConfigurationEventTypeXMLDOM sensorcfg = new ConfigurationEventTypeXMLDOM();
		sensorcfg.setRootElementName("log");
		sensorcfg.setSchemaResource(schemaURL.toString());
		epService.getEPAdministrator().getConfiguration()
		    .addEventType(LogEvent.LogEventStream, sensorcfg);
		
		return epService;
	}

	/**
	 * High-performance thread safe singleton instance implementation.
	 * 
	 */
	private static class InstanceHolder {
		/**
		 * singleton instance
		 */
		private static final ComplexEventProcessor instance = new ComplexEventProcessor();
	}

	public static ComplexEventProcessor getInstance() {
		return InstanceHolder.instance;
	}

	public void processEvent(LogDocument eventDoc) {
		String eventID = eventDoc.getLog().getId();
		
		// select event stream based on its id
		if(eventID.equals(AvailabilityEvent.AvailabilityStream)) {
			AvailabilityEvent event = AvailabilityEvent.parse(eventDoc);
			this.epService.getEPRuntime().sendEvent(event);
		} else if(eventID.equals(CpuUtilizationEvent.CpuUtilizationStream)) {
			CpuUtilizationEvent event = CpuUtilizationEvent.parse(eventDoc);
			this.epService.getEPRuntime().sendEvent(event);
		} else {
			this.processLogEvent(eventDoc);
		}
	}
	
	private void processLogEvent(LogDocument parse) {
		// Do not use xmlbeans parser, because it is not DOM 3 compliant
		//		Node node = parse.getDomNode();
		
		try {
			Document doc = convertXMLFromString(parse.toString());
			this.epService.getEPRuntime().sendEvent(doc);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public EPServiceProvider getProvider() {
		return epService;
	}
	
	private Document convertXMLFromString(String xml) throws ParserConfigurationException, SAXException, IOException
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    return builder.parse(new ByteArrayInputStream(xml.getBytes()));
	}
	
}
