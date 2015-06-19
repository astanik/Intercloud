package de.tu_berlin.cit.intercloud.xmpp.client.extension;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.dom2_builder.DOM2XmlPullBuilder;

public class XmlPullParserConverter {

	public static String convert2String(XmlPullParser parser) throws XmlPullParserException, IOException {
		DOM2XmlPullBuilder dom2XmlPullBuilder = new DOM2XmlPullBuilder();
		Element element = dom2XmlPullBuilder.parseSubTree(parser);
		
		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(element), new StreamResult(buffer));
			return buffer.toString();
		
		} catch (TransformerConfigurationException e) {
			throw new XmlPullParserException(e.getMessage());
		} catch (TransformerException e) {
			throw new XmlPullParserException(e.getMessage());
		}
	}

}
