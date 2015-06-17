package de.tu_berlin.cit.intercloud.xmpp.client;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;

public class RestIQ extends IQ {

	public static final String ELEMENT = "resource";
	public static final String NAMESPACE = "urn:xmpp:xml-rest";

	private final ResourceDocument doc;

	final private XmppURI uri;

	public RestIQ(XmppURI uri) {
		this(uri, ResourceDocument.Factory.newInstance());
	}

    public RestIQ(XmppURI uri, ResourceDocument doc) {
		super(ELEMENT, NAMESPACE);
		this.setStanzaId(StanzaIdUtil.newStanzaId());
		this.uri = uri;
		this.setType(Type.set);
		this.setTo(uri.getJID());
		this.doc = doc;
		
		if(this.doc.getResource() == null)
			this.doc.addNewResource().setPath(this.uri.getPath());

    }

	@Override
	protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
			IQChildElementXmlStringBuilder xml) {
		// set resource path
		xml.attribute("path", this.uri.getPath());
		xml.rightAngleBracket();

		if(this.doc.getResource().isSetMethod())
			xml.append(this.doc.getResource().getMethod().toString());
		else if(this.doc.getResource().isSetAction())
			xml.append(this.doc.getResource().getAction().toString());

		return xml;
	}


}
