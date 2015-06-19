package de.tu_berlin.cit.intercloud.xmpp.client;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.xmpp.client.extension.RestIQ;
import de.tu_berlin.cit.intercloud.xmpp.client.extension.XwadlIQ;
import de.tu_berlin.cit.intercloud.xmpp.rest.MethodInvocation;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public class XmppRestMethod extends MethodInvocation {

	protected final static Logger logger = LoggerFactory.getLogger(XmppRestMethod.class);

	private final XmppURI uri;
	
	private final XMPPConnection connection;

	public XmppRestMethod(XMPPConnection connection, XmppURI uri,
			ResourceDocument resourceDoc, de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method method) {
		super(resourceDoc, method);
		this.connection = connection;
		this.uri = uri;
	}

	public Representation invoke(Representation rep) throws XMPPErrorException, XmlException, SmackException {
		this.setRequestRepresentation(rep);
		return this.invoke();
	}

	public Representation invoke() throws XMPPErrorException, XmlException, SmackException {
		// create an set IQ stanza to uri
		RestIQ setIQ = new RestIQ(this.uri, this.getXmlDocument());
		
//		logger.info("invoke: the following stanza will be send: " + this.getXmlDocument().toString());

		// send stanza
		this.connection.sendStanza(setIQ);

		// wait for response
		StanzaFilter filter = new AndFilter(new IQReplyFilter(setIQ,
				connection));
		PacketCollector collector = connection
				.createPacketCollector(filter);
		IQ resultIQ = collector.nextResultOrThrow();
		ResourceDocument doc = null;
		if(resultIQ instanceof RestIQ) {
			// create rest doc
			doc = ((RestIQ) resultIQ).getResourceDocument();
		} else
			throw new SmackException("Wrong RestIQ has been passed");
		
//		logger.info("the following resource stanza had been received: " + doc.toString());

		// create representation
		return getPresentation(doc);
	}

	private Representation getPresentation(ResourceDocument xmlRepresentationDoc) throws XmlException {
		if(xmlRepresentationDoc.getResource().isSetMethod()) {
			Method method = xmlRepresentationDoc.getResource().getMethod();
			if(method.isSetResponse()) {
				return this.getResponseRepresentation(method);
			}
		}
		return null;
	}
}
