package de.tu_berlin.cit.intercloud.xmpp.client;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;

public class RestIQ extends IQ {

	public static final String ELEMENT = "resource";
	public static final String NAMESPACE = "urn:xmpp:xml-rest";

	protected RestIQ(String from, String to, Type type) {
		super(ELEMENT, NAMESPACE);
		setFrom(from);
		setTo(to);
		setType(type);
	}

	@Override
	protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
			IQChildElementXmlStringBuilder xml) {
		xml.rightAngleBracket();
		return xml;
	}

	public static RestIQ createRestPacket(String from, String to, Type type,
			ExtensionElement extension) {
		RestIQ pubSub = new RestIQ(from, to, type);
		pubSub.addExtension(extension);
		return pubSub;
	}
}
