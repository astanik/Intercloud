package de.tu_berlin.cit.intercloud.xmpp.client;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;

public abstract class RestIQ extends IQ {

	public static final String ELEMENT = "resource";
	public static final String NAMESPACE = "urn:xmpp:xml-rest";

	protected RestIQ() {
		super(ELEMENT, NAMESPACE);
		this.setStanzaId(StanzaIdUtil.newStanzaId());
	}

}
