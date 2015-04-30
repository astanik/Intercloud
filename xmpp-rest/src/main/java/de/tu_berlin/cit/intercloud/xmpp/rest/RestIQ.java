package de.tu_berlin.cit.intercloud.xmpp.rest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;

import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public class RestIQ extends IQ {

	/**
     * Constructs a new IQ using the specified type. A packet ID will
     * be automatically generated.
     *
     * @param type the IQ type.
     */
	public RestIQ(Type type) {
		super(type);
	}
	
	/**
     * Constructs a new IQ using the specified type and ID.
     *
     * @param ID the packet ID of the IQ.
     * @param type the IQ type.
     */
    public RestIQ(Type type, String ID) {
    	super(type, ID);
    }
    
    public RestIQ(Type type, String ID, ResourceTypeDocument doc) {
    	this(type, ID);
    	Document child;
		try {
			child = DocumentHelper.parseText(doc.toString());
	    	this.setChildElement(child.getRootElement());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public RestIQ(Type type, String ID, ResourceDocument doc) {
    	this(type, ID);
    	Document child;
		try {
			child = DocumentHelper.parseText(doc.toString());
	    	this.setChildElement(child.getRootElement());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
