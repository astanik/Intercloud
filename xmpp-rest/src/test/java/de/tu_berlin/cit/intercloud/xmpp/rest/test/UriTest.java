package de.tu_berlin.cit.intercloud.xmpp.rest.test;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;


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
	    	
		} catch (URISyntaxException e) {
	        Assert.fail(e.getMessage());
		}
	}


}
