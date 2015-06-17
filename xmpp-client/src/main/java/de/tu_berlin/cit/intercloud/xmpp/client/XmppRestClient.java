package de.tu_berlin.cit.intercloud.xmpp.client;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;

public class XmppRestClient {

	public static class XmppRestClientBuilder {
		public static XmppRestClient build(XMPPConnection connection,
				XmppURI uri) throws NotConnectedException, NoResponseException,
				XMPPErrorException {
			// create an get IQ stanza to uri
			IQ getIQ = new GetRequestIQ(uri);

			// send stanza
			connection.sendStanza(getIQ);

			// wait for response
			StanzaFilter filter = new AndFilter(new IQReplyFilter(getIQ,
					connection));
			PacketCollector collector = connection
					.createPacketCollector(filter);
			IQ resultIQ = collector.nextResultOrThrow();
			System.out.println("Received iq: " + resultIQ.toString());

			// create client
			return new XmppRestClient();
		}
	}

	private XmppRestClient() {

	}
}
