package de.tu_berlin.cit.intercloud.xmpp.client;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;

import de.tu_berlin.cit.intercloud.xmpp.rest.MethodInvocation;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;

public class XmppRestMethod extends MethodInvocation {

	private final XmppURI uri;
	
	private final XMPPConnection connection;

	public XmppRestMethod(XMPPConnection connection, XmppURI uri,
			ResourceDocument resourceDoc, de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method method) {
		super(resourceDoc, method);
		this.connection = connection;
		this.uri = uri;
	}

	public Representation invoke(Representation rep) throws NotConnectedException, NoResponseException, XMPPErrorException, XmlException {
		this.setRequestRepresentation(rep);
		return this.invoke();
	}

	public Representation invoke() throws NotConnectedException, NoResponseException, XMPPErrorException, XmlException {
		// create an set IQ stanza to uri
		RestIQ setIQ = new RestIQ(this.uri, this.getXmlDocument());

		// send stanza
		this.connection.sendStanza(setIQ);

		// wait for response
		StanzaFilter filter = new AndFilter(new IQReplyFilter(setIQ,
				connection));
		PacketCollector collector = connection
				.createPacketCollector(filter);
		IQ resultIQ = collector.nextResultOrThrow();
		System.out.println("Received iq: " + resultIQ.toString());

		// create representation
		return getPresentation(resultIQ);
	}

	private Representation getPresentation(IQ resultIQ) throws XmlException {
		ResourceDocument xmlRepresentationDoc = ResourceDocument.Factory.parse(resultIQ.getChildElementXML().toString());
		if(xmlRepresentationDoc.getResource().isSetMethod()) {
			Method method = xmlRepresentationDoc.getResource().getMethod();
			if(method.isSetResponse()) {
				return this.getResponseRepresentation(method);
			}
		}
		return null;
	}
}
