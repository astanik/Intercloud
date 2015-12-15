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

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public final class XmppURI implements Serializable {
	private static final long serialVersionUID = 8912597901674540333L;

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

	public String getNode() {
		return this.uri.getUserInfo();
	}

	public String getDomain() {
		return this.uri.getHost();
	}

	public String getResource() {
		return this.uri.getPath();
	}

	public String getJID() {
		StringBuilder jid = new StringBuilder("");
		String node = this.getNode();
		String domain = this.getDomain();
		String resource = this.getResource();

		if(node != null)
			jid.append(node).append("@");
		if(domain != null)
			jid.append(domain);
		if(resource != null)
			jid.append(resource);
		return jid.toString();
	}
	
	@Override
	public String toString() {
		return this.uri.toASCIIString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		XmppURI xmppURI = (XmppURI) o;

		return uri.equals(xmppURI.uri);

	}

	@Override
	public int hashCode() {
		return uri.hashCode();
	}
}
