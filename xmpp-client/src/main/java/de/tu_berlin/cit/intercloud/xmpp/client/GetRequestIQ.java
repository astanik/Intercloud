package de.tu_berlin.cit.intercloud.xmpp.client;


import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;

public class GetRequestIQ extends RestIQ {

	final private XmppURI uri;
	
	public GetRequestIQ(XmppURI uri) {
		super();
		this.uri = uri;
		this.setType(Type.get);
		this.setTo(uri.getJID());
	}

	@Override
	protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
			IQChildElementXmlStringBuilder xml) {
		// set resource path
		xml.attribute("path", this.uri.getPath());
		// mark that no further elements are present
		xml.setEmptyElement();
		return xml;
	}

}
