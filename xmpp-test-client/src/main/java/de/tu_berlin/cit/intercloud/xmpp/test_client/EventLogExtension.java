package de.tu_berlin.cit.intercloud.xmpp.test_client;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.Message;

import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;

public class EventLogExtension extends DefaultExtensionElement {

	private final LogDocument event;
	
	public EventLogExtension(LogDocument event) {
		super("log", "urn:xmpp:eventlog");
		this.event = event;
	}
	
	@Override
	public CharSequence toXML() {
		return this.event.toString();
	}

	public Message toMessage(String toJID) {
		Message message = new Message(toJID, Message.Type.normal);
        message.addExtension(this);
        return message;
	}

}
