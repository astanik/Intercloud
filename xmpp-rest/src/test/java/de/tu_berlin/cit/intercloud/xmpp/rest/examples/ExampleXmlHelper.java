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

package de.tu_berlin.cit.intercloud.xmpp.rest.examples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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

import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ExampleXmlHelper {

	private static final String xsdFolder = "src/main/xsd/";

	private static final String xmlFolder = "src/test/resources/de/tu_berlin/cit/intercloud/xmpp/rest/examples/";

	private static final String outFolder = "target/";

	public void validate(String xml, String xsd) throws SAXException,
			IOException, ParserConfigurationException {
		// parse an XML document into a DOM tree
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder parser = dbf.newDocumentBuilder();
		Document document = parser.parse(new File(xmlFolder + xml));

		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// load a WXS schema, represented by a Schema instance
		Source schemaFile = new StreamSource(new File(xsdFolder + xsd));
		Schema schema = factory.newSchema(schemaFile);

		// create a Validator instance, which can be used to validate an
		// instance document
		Validator validator = schema.newValidator();

		// validate the DOM tree
		validator.validate(new DOMSource(document));
	}

	public ResourceTypeDocument getXwadlDocument(String xml)
			throws XmlException, IOException {
		return ResourceTypeDocument.Factory.parse(new File(xmlFolder + xml));
	}

	public ResourceDocument getResourceDocument(String xml)
			throws XmlException, IOException {
		return ResourceDocument.Factory.parse(new File(xmlFolder + xml));
	}

	public void storeIQ(String doc, String xml) throws IOException {
		Writer out = new FileWriter(new File(outFolder + xml).getAbsoluteFile());
		try {
			out.write(doc);
		} finally {
			out.close();
		}
	}
}
