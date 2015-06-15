package de.tu_berlin.cit.intercloud.util.xmpp;

import java.net.URI;
import java.net.URISyntaxException;

public final class XmppURI {

	private final URI uri;

	public XmppURI(String uriString) throws URISyntaxException {
		this.uri = new URI(uriString);
	}
	
	public XmppURI(String jid, String restPath) throws URISyntaxException {
		this.uri = new URI("xmpp://" +
					jid +
					"#" +
					restPath);
	}
	
	public XmppURI(String component, String resource, String restPath) throws URISyntaxException {
		this.uri = new URI("xmpp",
				   component,
				   resource,
				   restPath);
	}
	
	public String getPath() {
		return this.uri.getFragment();
	}
	
	public String getJID() {
		String jid = "";
		if(this.uri.getUserInfo() != null)
			jid = jid + this.uri.getUserInfo() + "@";
		if(this.uri.getHost() != null)
			jid = jid + this.uri.getHost();
		if(this.uri.getPath() != null)
			jid = jid + this.uri.getPath();
		return jid;
	}
	
	@Override
	public String toString() {
		return this.uri.toASCIIString();
	}
}
