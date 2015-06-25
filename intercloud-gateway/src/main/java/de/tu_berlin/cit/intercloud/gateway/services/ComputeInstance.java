package de.tu_berlin.cit.intercloud.gateway.services;

import java.util.UUID;

import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.OcciText;

@PathID
public class ComputeInstance extends ResourceInstance {

//	final static public String InfrastructureSchema = AbstractKind.InfrastructureSchema + "compute";
//
//	public VirtualMachine(String id, String title, Kind kind, List<Mixin> mixins,
//			List<Link> links, String summary) {
//		super(id, title, kind, mixins, links, summary);
//	}
	
	private static UUID TemplateID = UUID.fromString("f509099b-0da9-4f96-8fe6-7b20f6614381");

	private final OcciText representation;
	
	public ComputeInstance() {
		this(new OcciText());
	}

	public ComputeInstance(OcciText rep) {
		this.representation = rep;
	}

	@XmppMethod(XmppMethod.GET)
	@Produces(value = OcciText.MEDIA_TYPE, serializer = OcciText.class)
	public OcciText getVMs() {
		return this.representation;
	}

	@XmppMethod(XmppMethod.DELETE)
	public void deleteVM() {
		this.getParent().removeResource(this);
	}

//	@Action()
	public Boolean start(String message) {
		// starting vm
		System.out.println("Stating vm with message: " + message);
		return true;
	}
	
//	@Action()
	public void stop(Integer delay) {
		// stop the vm after "delay" seconds
		System.out.println("Stopping vm in " + delay + " seconds");
	}
	
}