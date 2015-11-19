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

import java.net.URISyntaxException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.tu_berlin.cit.intercloud.gateway.services.Compute;
import de.tu_berlin.cit.intercloud.occi.core.OcciClient;
import de.tu_berlin.cit.intercloud.occi.core.OcciContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.MethodInvocation;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;


/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ContainerTest {


	@Test
	public void computeTest() {
	    try {
			XmppURI uri = new XmppURI("exchange.cit-mac1.cit.tu-berlin.de", "/occi");
			OcciContainer container = new OcciContainer(uri);
			System.out.println("Container base path: " + container.getPath());
			container.addResource(new Compute());
			ResourceInstance cmp = container.getResource("/compute");
			if(cmp == null)
				System.out.println("is null");
			Assert.assertTrue(cmp != null);
			System.out.println("Compute absolute path: " + cmp.getPath());
			ResourceTypeDocument doc = container.getXWADL("/compute");
			System.out.println(container.getSupportedTypes());
			// client test
			OcciClient client = new OcciClient(doc);
			Method method = client.getMethod(MethodType.GET, null, UriListText.MEDIA_TYPE);
			if(method != null) {
				MethodInvocation invocable = client.buildMethodInvocation(method);
//				List<Representation> rep = client.getRequestTemplates(method);
//				for(Representation representation : rep) {
//					invocable.setRequestRepresentation(representation);
				container.execute(invocable.getXmlDocument());
//				}
			}
		} catch (URISyntaxException e) {
	        Assert.fail(e.getMessage());
		}
	}

}
