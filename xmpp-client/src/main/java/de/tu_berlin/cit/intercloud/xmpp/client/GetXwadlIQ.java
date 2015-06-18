package de.tu_berlin.cit.intercloud.xmpp.client;


import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;

public class GetXwadlIQ extends IQ {

	public static final String ELEMENT = "resource_type";
	public static final String NAMESPACE = "urn:xmpp:rest-xwadl";

	final private XmppURI uri;
	
	public GetXwadlIQ(XmppURI uri) {
		super(ELEMENT, NAMESPACE);
		this.setStanzaId(StanzaIdUtil.newStanzaId());
		this.uri = uri;
		this.setType(Type.get);
		this.setTo(uri.getJID());
	}

	@Override
	protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
			IQChildElementXmlStringBuilder xml) {
		// set resource path
		xml.attribute("path", this.uri.getPath());
//		xml.rightAngleBracket();
		// mark that no further elements are present
		xml.setEmptyElement();
		return xml;
	}

}
