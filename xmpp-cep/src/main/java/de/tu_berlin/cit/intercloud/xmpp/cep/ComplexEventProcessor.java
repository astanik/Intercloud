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
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.xml.sax.SAXException;

import com.espertech.esper.client.ConfigurationEventTypeXMLDOM;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ComplexEventProcessor {

	protected final static Logger logger = LoggerFactory.getLogger(ComplexEventProcessor.class);

	/**
	 * singleton instance
	 */
	private static ComplexEventProcessor instance;

	/**
	 * Esper provider instance
	 */
	private final EPServiceProvider epService;
	
	/**
	 * Event Log xsd name
	 */
	private static final String eventLogFile= "eventlog.xsd";
	
	/**
	 * default constructor
	 * 
	 */
	private ComplexEventProcessor() {
		System.setProperty(DOMImplementationRegistry.PROPERTY,
				"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");
		
		URL schemaURL = ComplexEventProcessor.class.getClassLoader().getResource(eventLogFile);
		logger.info("xsd path is: " + schemaURL.toString());
		this.epService = EPServiceProviderManager.getDefaultProvider();
		// configure
		ConfigurationEventTypeXMLDOM sensorcfg = new ConfigurationEventTypeXMLDOM();
		sensorcfg.setRootElementName("log");
		sensorcfg.setSchemaResource(schemaURL.toString());
		epService.getEPAdministrator().getConfiguration()
		    .addEventType("LogEvent", sensorcfg);
	}

	public static synchronized ComplexEventProcessor getInstance() {
		if (ComplexEventProcessor.instance == null) {
			ComplexEventProcessor.instance = new ComplexEventProcessor();
		}
		return ComplexEventProcessor.instance;
	}

	public void processEvent(LogDocument parse) {
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
