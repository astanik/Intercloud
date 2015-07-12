/**
 * Copyright 2010-2015 Complex and Distributed IT Systems, TU Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tu_berlin.cit.intercloud.xmpp.rest;

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
	
	public XmppURI(URI uri) throws URISyntaxException {
		if(uri != null)
			this.uri = new URI(uri.toASCIIString());
		else
			throw new URISyntaxException("null","The uri object is null");
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
