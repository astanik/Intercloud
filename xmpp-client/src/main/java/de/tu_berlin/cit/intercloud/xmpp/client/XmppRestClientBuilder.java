package de.tu_berlin.cit.intercloud.xmpp.client;

import java.net.URI;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;

public class XmppRestClientBuilder {

	public static XmppRestClient build(XMPPConnection connection, URI uri) {
		// create an get IQ stanza to uri
		IQ getIQ = new IQ();
		
		// send stanza
		
		// wait for response
		StanzaFilter filter = new AndFilter(new IQReplyFilter(getIQ, connection));
        PacketCollector collector = connection.createPacketCollector(filter);
        IQ resultIQ = collector.nextResultOrThrow();
        System.out.println("Received iq: " + resultIQ.toString());

        // create client
		
		return null;
	}

}
