package de.tu_berlin.cit.intercloud.xmpp.client.extension;

import java.net.URISyntaxException;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.id.StanzaIdUtil;

import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ActionDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument;

public class RestIQ extends IQ {

	public static final String ELEMENT = "resource";
	public static final String NAMESPACE = "urn:xmpp:xml-rest";

	private final ResourceDocument doc;

	private XmppURI uri;
/*
	public RestIQ(XmppURI uri) {
		this(uri, ResourceDocument.Factory.newInstance());
	}
*/
	/**
	 * This constructor should only be called by the client builder
	 * @param uri
	 * @param doc
	 */
    public RestIQ(XmppURI uri, ResourceDocument doc) {
		this(doc);
		this.setStanzaId(StanzaIdUtil.newStanzaId());
		this.uri = uri;
		this.setType(Type.set);
		this.setTo(uri.getJID());
		
		if(this.doc.getResource() == null)
			this.doc.addNewResource().setPath(this.uri.getPath());
		
//		System.out.println("RestIQ was created with doc=" + this.doc.toString());

    }

	public RestIQ(ResourceDocument doc) {
		super(ELEMENT, NAMESPACE);
		this.doc = doc;
		try {
			this.uri = new XmppURI(doc.getResource().getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
			IQChildElementXmlStringBuilder xml) {
		// set resource path
		xml.attribute("path", this.uri.getPath());
		xml.rightAngleBracket();

		try {
			if(this.doc.getResource().isSetMethod()) {
//				System.out.println("method toString:" + this.doc.getResource().getMethod().toString());
//				System.out.println("method toxmlText:" + this.doc.getResource().getMethod().xmlText());
//				System.out.println("method getDomNode.toString:" + this.doc.getResource().getMethod().getDomNode().toString());
				// parse method to method document
				MethodDocument methDoc;
				methDoc = MethodDocument.Factory.parse(this.doc.getResource().getMethod().getDomNode());
				xml.append(methDoc.toString());
				// do not call the toString method of a method directly, like
				// xml.append(this.doc.getResource().getMethod().toString());
			} else if(this.doc.getResource().isSetAction()) {
				// parse action to action document
				ActionDocument actDoc;
				actDoc = ActionDocument.Factory.parse(this.doc.getResource().getAction().getDomNode());
				xml.append(actDoc.toString());
				// do not call the toString method of an action directly, like
				// xml.append(this.doc.getResource().getAction().toString());
			}
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println("generated xml:" + xml);

		return xml;
	}

	public ResourceDocument getResourceDocument() {
		return this.doc;
	}


}
