package de.tu_berlin.cit.intercloud.xmpp.rest;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.RequestDocument.Request;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;

public class MethodInvocation {

	private final ResourceDocument doc;
	
	public MethodInvocation(ResourceDocument resourceDoc, de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method method) {
		this.doc = resourceDoc;
		// set method type
		this.doc.getResource().addNewMethod().setType(MethodType.Enum.forString(method.getType().toString()));
		// set request
		if(method.isSetRequest()) {
			this.doc.getResource().getMethod().addNewRequest().setMediaType(method.getRequest().getMediaType());
		}
		// set response
		if(method.isSetResponse()) {
			this.doc.getResource().getMethod().addNewResponse().setMediaType(method.getResponse().getMediaType());
		}
	}
	
	public ResourceDocument getXmlDocument() {
		return this.doc;
	}

	public void setRequestRepresentation(Representation rep) {
		if(this.doc.getResource().getMethod().isSetRequest()) {
			Request request = this.doc.getResource().getMethod().getRequest();
			StringBuilder builder = new StringBuilder();
			builder = rep.writeRepresentation(builder);
			request.setRepresentation(builder.toString());
		}
	}
}
