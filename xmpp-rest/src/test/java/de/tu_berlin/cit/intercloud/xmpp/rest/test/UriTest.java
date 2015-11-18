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

package de.tu_berlin.cit.intercloud.xmpp.rest.test;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;


/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class UriTest {

	@Test
	public void componentJID() {
	    try {
	    	XmppURI uri = new XmppURI(
								"exchange.cit.tu-berlin.de",
								"/rest",
								"/occi/compute");
	    	
	    	System.out.println("Full component uri= " + uri.toString());
	    	Assert.assertEquals(uri.getJID(), "exchange.cit.tu-berlin.de/rest");
	    	System.out.println("Component jid= " + uri.getJID());
	    	Assert.assertEquals(uri.getPath(), "/occi/compute");
	    	System.out.println("REST component path= " + uri.getPath());
	    	
	    	XmppURI gen = new XmppURI(uri.toString());
	    	Assert.assertEquals(gen.toString(), uri.toString());
	    	gen = new XmppURI(uri.getJID(), uri.getPath());
	    	Assert.assertEquals(gen.toString(), uri.toString());

			Assert.assertEquals("xmpp://exchange.cit.tu-berlin.de/rest#/occi/compute", uri.toString());
			Assert.assertNull(uri.getNode());
			Assert.assertEquals("exchange.cit.tu-berlin.de", uri.getDomain());
			Assert.assertEquals("/rest", uri.getResource());
		} catch (URISyntaxException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void userJID() {
	    try {
	    	XmppURI uri = new XmppURI(
								"alex@cit.tu-berlin.de/client",
								"/occi/meter");
	    	
	    	System.out.println("Full client uri= " + uri.toString());
	    	Assert.assertEquals(uri.getJID(), "alex@cit.tu-berlin.de/client");
	    	System.out.println("Client jid= " + uri.getJID());
	    	Assert.assertEquals(uri.getPath(), "/occi/meter");
	    	System.out.println("REST client path= " + uri.getPath());
	    	
	    	XmppURI gen = new XmppURI(uri.toString());
	    	Assert.assertEquals(gen.toString(), uri.toString());

			Assert.assertEquals("xmpp://alex@cit.tu-berlin.de/client#/occi/meter", uri.toString());
			Assert.assertEquals("alex", uri.getNode());
			Assert.assertEquals("cit.tu-berlin.de", uri.getDomain());
			Assert.assertEquals("/client", uri.getResource());
		} catch (URISyntaxException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void simpleUserJID() throws URISyntaxException {
		XmppURI uri = new XmppURI("user@cit.tu-berlin.de", "");

		Assert.assertEquals("xmpp://user@cit.tu-berlin.de#", uri.toString());
		Assert.assertEquals("user", uri.getNode());
		Assert.assertEquals("cit.tu-berlin.de", uri.getDomain());
		Assert.assertEquals("", uri.getResource());
		Assert.assertEquals("", uri.getPath());
		Assert.assertEquals("user@cit.tu-berlin.de", uri.getJID());
	}
}
