package de.tu_berlin.cit.intercloud.xmpp.client;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public abstract class RestIQ extends IQ {

	public static final String ELEMENT = "resource";
	public static final String NAMESPACE = "urn:xmpp:xml-rest";

	protected Element restElement = null;
	
	protected RestIQ() {
		super(ELEMENT, NAMESPACE);
		this.setStanzaId(StanzaIdUtil.newStanzaId());
	}

	/**
     * Constructs a new IQ using the specified type and ID.
     *
     * @param ID the packet ID of the IQ.
     * @param type the IQ type.
     */
    public RestIQ(Type type) {
    	this();
		this.setType(type);
    }

    public RestIQ(Type type, ResourceTypeDocument doc) {
    	this(type);
    	Document child;
		try {
			child = DocumentHelper.parseText(doc.toString());
	    	this.restElement = child.getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public RestIQ(Type type, ResourceDocument doc) {
    	this(type);
    	Document child;
		try {
			child = DocumentHelper.parseText(doc.toString());
	    	this.restElement = child.getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


}
