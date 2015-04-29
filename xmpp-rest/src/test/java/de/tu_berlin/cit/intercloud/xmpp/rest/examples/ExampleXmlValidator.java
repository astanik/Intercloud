package de.tu_berlin.cit.intercloud.xmpp.rest.examples;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ExampleXmlValidator {
	
	private static final String xsdFolder = "src/main/xsd/";
	
	private static final String xmlFolder = "src/test/resources/de/tu_berlin/cit/intercloud/xmpp/rest/examples/";
	

	public void validate(String xml, String xsd) throws SAXException, IOException, ParserConfigurationException {
		// parse an XML document into a DOM tree
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
	    DocumentBuilder parser = dbf.newDocumentBuilder();
	    Document document = parser.parse(new File(xmlFolder + xml));

	    // create a SchemaFactory capable of understanding WXS schemas
	    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

	    // load a WXS schema, represented by a Schema instance
	    Source schemaFile = new StreamSource(new File(xsdFolder + xsd));
	    Schema schema = factory.newSchema(schemaFile);

	    // create a Validator instance, which can be used to validate an instance document
	    Validator validator = schema.newValidator();

	    // validate the DOM tree
        validator.validate(new DOMSource(document));
	}

}
