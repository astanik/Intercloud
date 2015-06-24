package de.tu_berlin.cit.intercloud.xmpp.rest.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.tu_berlin.cit.intercloud.gateway.services.Compute;
import de.tu_berlin.cit.intercloud.xmpp.rest.MethodInvocation;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceClient;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;


public class ContainerTest {


	@Test
	public void computeTest() {
	    try {
			XmppURI uri = new XmppURI("exchange.cit-mac1.cit.tu-berlin.de", "/occi");
			ResourceContainer container = new ResourceContainer(uri);
			System.out.println("Container base path: " + container.getPath());
			container.addResource(new Compute());
			ResourceInstance cmp = container.getResource("/compute");
			if(cmp == null)
				System.out.println("is null");
			Assert.assertTrue(cmp != null);
			System.out.println("Compute absolute path: " + cmp.getPath());
			ResourceTypeDocument doc = container.getXWADL("/compute");
			Writer out = new FileWriter(new File("target/computeXWADL.xml").getAbsoluteFile());
			doc.save(out);
			out.close();
			// client test
			ResourceClient client = new ResourceClient(doc);
			List<Method> list = client.getMethods(MethodType.POST);
			for(Method method : list) {
				MethodInvocation invocable = client.buildMethodInvocation(method);
				List<Representation> rep = client.getRequestTemplates(method);
				for(Representation representation : rep) {
					invocable.setRequestRepresentation(representation);
					container.execute(invocable.getXmlDocument());
				}
			}
		} catch (URISyntaxException e) {
	        Assert.fail(e.getMessage());
		} catch (IOException e) {
	        Assert.fail(e.getMessage());
		}
	}

}
