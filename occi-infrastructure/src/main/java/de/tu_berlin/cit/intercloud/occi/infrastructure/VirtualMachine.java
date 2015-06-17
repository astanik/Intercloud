package de.tu_berlin.cit.intercloud.occi.infrastructure;

import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.OcciText;

@PathID
public class VirtualMachine extends ResourceInstance {

//	final static public String InfrastructureSchema = AbstractKind.InfrastructureSchema + "compute";
//
//	public VirtualMachine(String id, String title, Kind kind, List<Mixin> mixins,
//			List<Link> links, String summary) {
//		super(id, title, kind, mixins, links, summary);
//	}

	private final OcciText representation;
	
	public VirtualMachine() {
		this(new OcciText());
	}

	public VirtualMachine(OcciText rep) {
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