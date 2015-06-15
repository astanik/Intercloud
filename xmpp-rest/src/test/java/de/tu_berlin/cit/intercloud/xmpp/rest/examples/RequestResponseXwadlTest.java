package de.tu_berlin.cit.intercloud.xmpp.rest.examples;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class RequestResponseXwadlTest {
	
	private static final String xsdFile = "rest-xwadl.xsd";
	
	@Test
	public void computeExplorationRequest() {
		ExampleXmlHelper validator = new ExampleXmlHelper();
	    try {
	    	validator.validate("computeExplorationRequest.xml", xsdFile);
	    } catch (SAXException e) {
	        Assert.fail(e.getMessage());
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (ParserConfigurationException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void computeExplorationResponse() {
		ExampleXmlHelper validator = new ExampleXmlHelper();
	    try {
	    	validator.validate("computeExplorationResponse.xml", xsdFile);
	    } catch (SAXException e) {
	        Assert.fail(e.getMessage());
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (ParserConfigurationException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void grammarsExampleResponse() {
		ExampleXmlHelper validator = new ExampleXmlHelper();
	    try {
	    	validator.validate("grammarsExampleResponse.xml", xsdFile);
	    } catch (SAXException e) {
	        Assert.fail(e.getMessage());
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (ParserConfigurationException e) {
	        Assert.fail(e.getMessage());
		}
	}


}
