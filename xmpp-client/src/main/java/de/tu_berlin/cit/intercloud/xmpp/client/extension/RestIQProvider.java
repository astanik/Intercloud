package de.tu_berlin.cit.intercloud.xmpp.client.extension;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;

public class RestIQProvider extends IQProvider<RestIQ> {

	@Override
	public RestIQ parse(XmlPullParser parser, int initialDepth)
			throws XmlPullParserException, IOException, SmackException {
		// convert to string
		String str = XmlPullParserConverter.convert2String(parser);
		// create resource doc
		try {
			ResourceDocument doc = ResourceDocument.Factory.parse(str);
			return new RestIQ(doc);
		} catch (XmlException e) {
			throw new XmlPullParserException(e.getMessage());
		}
	}

}
