package de.tu_berlin.cit.intercloud.root.services;

import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.OcciXml;

@PathID
public class TemplateInstance extends ResourceInstance {

	private final OcciXml representation;
	
	public TemplateInstance(OcciXml template) {
		this.representation = template;
	}

	@XmppMethod(XmppMethod.GET)
	@Produces(value = OcciXml.MEDIA_TYPE, serializer = OcciXml.class)
	public OcciXml getOcciXml() {
		return this.representation;
	}
	
	@XmppMethod(XmppMethod.DELETE)
	public void deleteTemplate() {
		this.getParent().removeResource(this);
	}

}
