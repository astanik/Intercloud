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
import java.net.URISyntaxException;

import org.apache.xmlbeans.XmlException;
//import org.apache.xmlbeans.XmlException;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
//import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ.Type;
//import de.tu_berlin.cit.intercloud.xmpp.rest.RestIQ;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;

public class RequestResponseXmlRestTest {
/*
	private static final String folderChange = "";
	
	private static final String xsdFile = "xml-rest.xsd";

	@Test
	public void simpleRequestIQ() {
		ExampleXmlHelper parser = new ExampleXmlHelper();
	    try {
	    	ResourceDocument doc = parser.getResourceDocument("simpleRequest.xml");
	    	XmppURI uri = new XmppURI("company-a.com/openstack", doc.getResource().getPath());
	    	RestIQ iq = new RestIQ(uri, doc);
	    	iq.setFrom("requester@company-b.com/rest-client");
	    	parser.storeIQ(iq.toString(), "simpleRequestIQ.xml");
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (XmlException e) {
	        Assert.fail(e.getMessage());
		} catch (URISyntaxException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void simpleResponseIQ() {
		ExampleXmlHelper parser = new ExampleXmlHelper();
	    try {
	    	ResourceDocument doc = parser.getResourceDocument("simpleResponse.xml");
	    	XmppURI uri = new XmppURI("requester@company-b.com/rest-client", doc.getResource().getPath());
	    	RestIQ iq = new RestIQ(uri, doc);
	    	iq.setFrom("company-a.com/openstack");
	    	parser.storeIQ(iq.toString(), "simpleResponseIQ.xml");
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (XmlException e) {
	        Assert.fail(e.getMessage());
		} catch (URISyntaxException e) {
	        Assert.fail(e.getMessage());
		}
	}
*/
/*	
	@Test
	public void computeCreateRequestIQ() {
		ExampleXmlHelper parser = new ExampleXmlHelper();
	    try {
	    	ResourceDocument doc = parser.getResourceDocument("computeCreateRequest.xml");
	    	RestIQ iq = new RestIQ(Type.set, "rest2", doc);
	    	iq.setFrom("requester@company-b.com/rest-client");
	    	iq.setTo("company-a.com/openstack");
	    	parser.storeIQ(iq.toString(), "computeCreateRequestIQ.xml");
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (XmlException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void computeCreateResponseIQ() {
		ExampleXmlHelper parser = new ExampleXmlHelper();
	    try {
	    	ResourceDocument doc = parser.getResourceDocument("computeCreateResponse.xml");
	    	RestIQ iq = new RestIQ(Type.result, "rest2", doc);
	    	iq.setFrom("company-a.com/openstack");
	    	iq.setTo("requester@company-b.com/rest-client");
	    	parser.storeIQ(iq.toString(), "computeCreateResponseIQ.xml");
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (XmlException e) {
	        Assert.fail(e.getMessage());
		}
	}
*/
}
