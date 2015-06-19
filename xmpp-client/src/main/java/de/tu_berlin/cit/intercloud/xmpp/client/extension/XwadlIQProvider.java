package de.tu_berlin.cit.intercloud.xmpp.client.extension;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public class XwadlIQProvider extends IQProvider<XwadlIQ> {

	@Override
	public XwadlIQ parse(XmlPullParser parser, int initialDepth)
			throws XmlPullParserException, IOException, SmackException {
		// convert to string
		String str = XmlPullParserConverter.convert2String(parser);
		// create xwadl
		try {
			ResourceTypeDocument xwadl = ResourceTypeDocument.Factory.parse(str);
			return new XwadlIQ(xwadl);
		} catch (XmlException e) {
			throw new XmlPullParserException(e.getMessage());
		}
	}

}
