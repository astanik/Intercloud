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

package de.tu_berlin.cit.intercloud.xmpp.client;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.packet.IQ.Type;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public class RequestResponseXwadlTest {
/*
	private static final String xsdFile = "rest-xwadl.xsd";
	
	@Test
	public void computeExplorationRequestIQ() {
		ExampleXmlHelper parser = new ExampleXmlHelper();
	    try {
	    	ResourceTypeDocument doc = parser.getXwadlDocument("computeExplorationRequest.xml");
	    	RestIQ iq = new RestIQ(Type.get, doc);
	    	iq.setFrom("requester@company-b.com/rest-client");
	    	iq.setTo("company-a.com/openstack");
	    	parser.storeIQ(iq.toString(), "computeExplorationRequestIQ.xml");
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (XmlException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void computeExplorationResponseIQ() {
		ExampleXmlHelper parser = new ExampleXmlHelper();
	    try {
	    	ResourceTypeDocument doc = parser.getXwadlDocument("computeExplorationResponse.xml");
	    	RestIQ iq = new RestIQ(Type.result, "rest1", doc);
	    	iq.setFrom("company-a.com/openstack");
	    	iq.setTo("requester@company-b.com/rest-client");
	    	parser.storeIQ(iq.toString(), "computeExplorationResponseIQ.xml");
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (XmlException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void grammarsExampleResponseIQ() {
		ExampleXmlHelper parser = new ExampleXmlHelper();
	    try {
	    	ResourceTypeDocument doc = parser.getXwadlDocument("grammarsExampleResponse.xml");
	    	RestIQ iq = new RestIQ(Type.result, "rest1", doc);
	    	iq.setFrom("responder@company-a.com");
	    	iq.setTo("requester@company-b.com/rest-client");
	    	parser.storeIQ(iq.toString(), "grammarsExampleResponseIQ.xml");
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (XmlException e) {
	        Assert.fail(e.getMessage());
		}
	}
*/
}
