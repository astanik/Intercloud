package de.tu_berlin.cit.intercloud.xmpp.client;

import org.jivesoftware.smack.packet.ExtensionElement;

import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument.Method;

public class MethodExtension implements ExtensionElement {

	private final MethodDocument doc;
	
	public MethodExtension(String name) {
		doc = MethodDocument.Factory.newInstance();
		Method mes = doc.addNewMethod();
		mes.setName(name);
	}
	@Override
	public String getElementName() {
		return "Method";
	}

	@Override
	public CharSequence toXML() {
		return doc.toString();
	}

	@Override
	public String getNamespace() {
		return "";
	}

}
