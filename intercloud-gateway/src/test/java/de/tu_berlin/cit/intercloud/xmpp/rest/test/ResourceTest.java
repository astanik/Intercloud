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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import de.tu_berlin.cit.intercloud.gateway.services.Compute;
import de.tu_berlin.cit.intercloud.gateway.services.Manager;
import de.tu_berlin.cit.intercloud.gateway.services.Network;
import de.tu_berlin.cit.intercloud.gateway.services.Storage;
import de.tu_berlin.cit.intercloud.occi.core.OcciXwadlPlugin;
import de.tu_berlin.cit.intercloud.xmpp.cep.sensor.Sensor;
import de.tu_berlin.cit.rwx4j.container.ResourceContainer;
import de.tu_berlin.cit.rwx4j.container.ResourceInstance;
import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 * @author Daniel Thilo Schroeder <daniel.schroeder@mailbox.tu-berlin.de>
 */
public class ResourceTest {

	private static final String testURI = "test.cit.tu-berlin.de";

	private void writeXWADL(XwadlDocument doc, String filePrefix) {
		try {
			Writer out = new FileWriter(
					new File("target/" + filePrefix + "XWADL.xml").getAbsoluteFile());
			doc.save(out);
			out.close();
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void computeTest() {
		try {
			XmppURI uri = new XmppURI(testURI, "/occi");
			ResourceContainer container = new ResourceContainer(uri);
			container.addPlugin(new OcciXwadlPlugin());
			container.addResource(new Compute());
			ResourceInstance cmp = container.getResource("/compute");
			Assert.assertTrue(cmp != null);
			writeXWADL(container.getXWADL("/compute"), "compute");
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void managerTest() {
		try {
			XmppURI uri = new XmppURI(testURI, "/occi");
			ResourceContainer container = new ResourceContainer(uri);
			container.addPlugin(new OcciXwadlPlugin());
			container.addResource(new Manager());
			ResourceInstance cmp = container.getResource("/manager");
			Assert.assertTrue(cmp != null);
			writeXWADL(container.getXWADL("/manager"), "manager");
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void sensorTest() {
		try {
			XmppURI uri = new XmppURI(testURI, "/occi");
			ResourceContainer container = new ResourceContainer(uri);
			container.addPlugin(new OcciXwadlPlugin());
			container.addResource(new Sensor());
			ResourceInstance cmp = container.getResource("/sensor");
			Assert.assertTrue(cmp != null);
			writeXWADL(container.getXWADL("/sensor"), "sensor");
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void networkTest() {
		try {
			XmppURI uri = new XmppURI(testURI, "/occi");
			ResourceContainer container = new ResourceContainer(uri);
			container.addPlugin(new OcciXwadlPlugin());
			container.addResource(new Network());
			ResourceInstance cmp = container.getResource("/network");
			Assert.assertTrue(cmp != null);
			writeXWADL(container.getXWADL("/network"), "network");
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void storageTest() {
		try {
			XmppURI uri = new XmppURI(testURI, "/occi");
			ResourceContainer container = new ResourceContainer(uri);
			container.addPlugin(new OcciXwadlPlugin());
			container.addResource(new Storage());
			ResourceInstance cmp = container.getResource("/storage");
			Assert.assertTrue(cmp != null);
			writeXWADL(container.getXWADL("/storage"), "storage");
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}
}
