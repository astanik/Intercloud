package de.tu_berlin.cit.intercloud.xmpp.client;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class RepresentationProvider extends IQProvider<RestIQ> {

	@Override
	public RestIQ parse(XmlPullParser arg0, int arg1)
			throws XmlPullParserException, IOException, SmackException {
		
		return null;
	}

}
