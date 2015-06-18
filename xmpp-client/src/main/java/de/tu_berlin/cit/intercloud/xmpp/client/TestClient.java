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

package de.tu_berlin.cit.intercloud.xmpp.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

import de.tu_berlin.cit.intercloud.util.monitoring.PerformanceMeter;
import de.tu_berlin.cit.intercloud.xmpp.rest.MethodInvocation;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.OcciText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.client.XmppRestClient;

public class TestClient {

	private static String testComponent = "exchange.intercloud.cit.tu-berlin.de";

	private static String computePath = "/compute";

	private final AbstractXMPPConnection connection;
	
	public TestClient(AbstractXMPPConnection connection) {
		this.connection = connection;
	}
	
	public void performTest() throws FileNotFoundException, UnsupportedEncodingException, 
			URISyntaxException, NotConnectedException, NoResponseException, XMPPErrorException, XmlException {
		
		// create files
		PrintWriter flavorWriter = new PrintWriter("getFlavor.txt", "UTF-8");
		flavorWriter.println("Count " + PerformanceMeter.getHead());
		PrintWriter createWriter = new PrintWriter("postCompute.txt", "UTF-8");
		createWriter.println("Count " + PerformanceMeter.getHead());
		PrintWriter deleteWriter = new PrintWriter("deleteCompute.txt", "UTF-8");
		deleteWriter.println("Count " + PerformanceMeter.getHead());

		// iterate over resources on server
		for (int r = 0; r < 100; r++) {
			OcciText representation = null;

			// create performance meter
			PerformanceMeter flavorMeter = new PerformanceMeter();
			PerformanceMeter createMeter = new PerformanceMeter();
			PerformanceMeter deleteMeter = new PerformanceMeter();
	
			// measure 50 times
			for (int i = 0; i < 50; i++) {
				XmppURI uri = new XmppURI(testComponent, computePath);
				XmppURI delUri = null;
				// get flavor
				XmppRestClient client = XmppRestClient.XmppRestClientBuilder.build(connection, uri);
				Method method = client.getMethod(MethodType.POST, new OcciText(), new UriText());
				if(method != null) {
					flavorMeter.startTimer(i);
					List<Representation> rep = client.getRequestTemplates(method);
					flavorMeter.stopTimer(i);
					if(rep.size() > 0) {
						if(rep.get(0) instanceof OcciText) {
							representation = (OcciText) rep.get(0);
							System.out.println("========Representation:========");
							System.out.println(representation);
							// create vm
							XmppRestMethod invocable = client.buildMethodInvocation(method);
							createMeter.startTimer(i);
							Representation vmURI = invocable.invoke(representation);
							createMeter.stopTimer(i);
							System.out.println("============VM URI:============");
							System.out.println(vmURI);
							if(vmURI instanceof UriText) {
								delUri = new XmppURI(((UriText)vmURI).getUri());
							}
						}
					}
				}
				// delete vm
				client = XmppRestClient.XmppRestClientBuilder.build(connection, delUri);
				method = client.getMethod(MethodType.DELETE, null, null);
				if(method != null) {
					XmppRestMethod invocable = client.buildMethodInvocation(method);
					deleteMeter.startTimer(i);
					Representation message = invocable.invoke();
					deleteMeter.stopTimer(i);
					System.out.println("===========Message:============");
					if(message != null)
						System.out.println(message);
				}
			}

			System.out.println(r + " resources available");

			// write sample to file
			System.out.println("===========GET flavor:============");
			flavorMeter.print();
			flavorWriter.println(r + " " + flavorMeter.toString());
			System.out.println("===========POST VM:============");
			createMeter.print();
			createWriter.println(r + " " + createMeter.toString());
			System.out.println("===========DELETE VM:============");
			deleteMeter.print();
			deleteWriter.println(r + " " + deleteMeter.toString());

			// create a new resources and continue
			{
				XmppURI uri = new XmppURI(testComponent, computePath);
				XmppRestClient client = XmppRestClient.XmppRestClientBuilder.build(connection, uri);
				Method method = client.getMethod(MethodType.POST, new OcciText(), new UriText());
				if(method != null) {
					List<Representation> rep = client.getRequestTemplates(method);
					if(rep.size() > 0) {
						if(rep.get(0) instanceof OcciText) {
							representation = (OcciText) rep.get(0);
							System.out.println("========Representation:========");
							System.out.println(representation);
							// create vm
							XmppRestMethod invocable = client.buildMethodInvocation(method);
							Representation vmURI = invocable.invoke(representation);
							System.out.println("============VM URI:============");
							System.out.println(vmURI);
						}
					}
				}
			}
		}
				
		// close file writer
		flavorWriter.close();
		createWriter.close();
		deleteWriter.close();
	}
		
}