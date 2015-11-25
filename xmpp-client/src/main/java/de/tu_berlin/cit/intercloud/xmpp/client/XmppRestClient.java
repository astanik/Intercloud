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

package de.tu_berlin.cit.intercloud.xmpp.client;

import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.occi.client.OcciClient;
import de.tu_berlin.cit.intercloud.occi.core.OcciListXml;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.GetXwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType.Enum;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class XmppRestClient extends OcciClient {


	protected final static Logger logger = LoggerFactory.getLogger(XmppRestClient.class);

	private final XmppURI uri;
	
	private final XMPPConnection connection;
	
	private XmppRestClient(XMPPConnection connection, XmppURI uri, ResourceTypeDocument xwadl) {
		super(xwadl);
		this.connection = connection;
		this.uri = uri;
//		logger.info("A new rest client has been build with uri=" + uri.toString()
//				+ " and xwadl=" + xwadl.toString());
	}

	@Override
	public XmppRestMethod buildMethodInvocation(Method method) {
		ResourceDocument resourceDoc = super.createBasicResourceDocument();
		return new XmppRestMethod(this.connection, this.uri, resourceDoc, method);
	}

	
	public static class XmppRestClientBuilder {
		public static XmppRestClient build(XMPPConnection connection,
				XmppURI uri) throws XMPPErrorException, XmlException, SmackException {
			logger.info("building rest client for uri=" + uri.toString());
			// create an get IQ stanza to uri
			IQ getIQ = new GetXwadlIQ(uri);

			// send stanza
			connection.sendStanza(getIQ);
			logger.info("the following stanza had been send: " + getIQ.toString());
			// wait for response
			StanzaFilter filter = new AndFilter(new IQReplyFilter(getIQ,
					connection));
			PacketCollector collector = connection
					.createPacketCollector(filter);
			IQ resultIQ = collector.nextResultOrThrow();
			ResourceTypeDocument xwadl = null;
			if(resultIQ instanceof XwadlIQ) {
				// create xwadl
				xwadl = ((XwadlIQ) resultIQ).getXwadl();
			} else
				throw new SmackException("Wrong IQ has been passed");
			
			logger.info("the following stanza had been received: " + xwadl.toString());

			// create client
			return new XmppRestClient(connection, uri, xwadl);
		}
	}


	public Method getMethod(Enum type, OcciXml occiXml, OcciListXml occiListXml) {
		String requestMediaType = null == occiXml ? null : OcciXml.MEDIA_TYPE;
		String responseMediaType = null == occiListXml ? null : OcciListXml.MEDIA_TYPE;
		return getMethod(type, requestMediaType, responseMediaType);
	}

	public Method getMethod(final Enum methodType, final String requestMediaType, final String responseMediaType) {
		List<Method> list = this.getMethods(methodType);
		for(Method method : list) {
			boolean requestMatch = false;
			if(method.isSetRequest() && null != requestMediaType) {
				if (requestMediaType.equals(method.getRequest().getMediaType())) {
					requestMatch = true;
				}
			}
			if(!method.isSetRequest() && null == requestMediaType) {
				requestMatch = true;
			}

			if (requestMatch) {
				if (method.isSetResponse() && null != responseMediaType) {
					if (responseMediaType.equals(method.getResponse().getMediaType())) {
						return method;
					}
				}
				if (!method.isSetResponse() && null == responseMediaType) {
					return method;
				}
			}
		}

		return null;
	}

}
