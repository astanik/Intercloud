package de.tu_berlin.cit.intercloud.xmpp.client;

import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;

import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceClient;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.OcciText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType.Enum;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;

public class XmppRestClient extends ResourceClient {


	private final XmppURI uri;
	
	private final XMPPConnection connection;
	
	private XmppRestClient(XMPPConnection connection, XmppURI uri, ResourceTypeDocument xwadl) {
		super(xwadl);
		this.connection = connection;
		this.uri = uri;
	}

	@Override
	public XmppRestMethod buildMethodInvocation(Method method) {
		ResourceDocument resourceDoc = super.createBasicResourceDocument();
		return new XmppRestMethod(this.connection, this.uri, resourceDoc, method);
	}

	
	public static class XmppRestClientBuilder {
		public static XmppRestClient build(XMPPConnection connection,
				XmppURI uri) throws NotConnectedException, NoResponseException,
				XMPPErrorException, XmlException {
			// create an get IQ stanza to uri
			IQ getIQ = new GetXwadlIQ(uri);

			// send stanza
			connection.sendStanza(getIQ);

			// wait for response
			StanzaFilter filter = new AndFilter(new IQReplyFilter(getIQ,
					connection));
			PacketCollector collector = connection
					.createPacketCollector(filter);
			IQ resultIQ = collector.nextResultOrThrow();
			System.out.println("Received iq: " + resultIQ.toString());

			// create xwadl
			ResourceTypeDocument xwadl = ResourceTypeDocument.Factory.parse(resultIQ.getChildElementXML().toString());
			// create client
			return new XmppRestClient(connection, uri, xwadl);
		}
	}


	public Method getMethod(Enum type, OcciText occiText, UriText uriText) {
		List<Method> list = this.getMethods(type);
		for(Method method : list) {
			Boolean requestMatch = false;
			if(method.isSetRequest() && occiText != null)
				if(method.getRequest().getMediaType().equals(occiText.MEDIA_TYPE))
					requestMatch = true;
			
			if(!method.isSetRequest() && occiText == null)
				requestMatch = true;
			
			if(requestMatch && method.isSetResponse() && uriText != null)
				if(method.getResponse().getMediaType().equals(uriText.MEDIA_TYPE))
					return method;

			if(requestMatch && !method.isSetResponse() && uriText == null)
				return method;
		}
		return null;
	}

}
