package de.tu_berlin.cit.intercloud.xmpp.rest.examples;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class RequestResponseXmlRestTest {

	private static final String xsdFile = "xml-rest.xsd";

	@Test
	public void simpleRequest() {
		ExampleXmlValidator validator = new ExampleXmlValidator();
	    try {
	    	validator.validate("simpleRequest.xml", xsdFile);
	    } catch (SAXException e) {
	        Assert.fail(e.getMessage());
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (ParserConfigurationException e) {
	        Assert.fail(e.getMessage());
		}
	}

	@Test
	public void simpleResponse() {
		ExampleXmlValidator validator = new ExampleXmlValidator();
	    try {
	    	validator.validate("simpleResponse.xml", xsdFile);
	    } catch (SAXException e) {
	        Assert.fail(e.getMessage());
	    } catch (IOException e) {
	        Assert.fail(e.getMessage());
		} catch (ParserConfigurationException e) {
	        Assert.fail(e.getMessage());
		}
	}

}