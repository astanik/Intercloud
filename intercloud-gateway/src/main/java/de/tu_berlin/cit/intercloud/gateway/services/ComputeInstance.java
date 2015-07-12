package de.tu_berlin.cit.intercloud.gateway.services;

import java.util.UUID;

import de.tu_berlin.cit.intercloud.occi.core.OcciText;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;

@PathID
@Summary("This resource allows for manage compute instances, e.g. creating virtual machines.")
@Kind(ComputeKind.class)
public class ComputeInstance extends Resource {

	private static UUID TemplateID = UUID.fromString("f509099b-0da9-4f96-8fe6-7b20f6614381");

	public ComputeInstance(OcciXml rep) {
		super(rep);
	}


//	@Action("start")
//	@Result()
	public Boolean start(String message) {
		// starting vm
		System.out.println("Stating vm with message: " + message);
		return true;
	}
	
//	@Action(value = "stop", documentation = "Stop this virtual machine")
//	@Result(documentation = "Returns true if the vm has been stopped successfully")
//	public Boolean stop( @Parameter(value = method, documentation = "The method used for stopping this vm") String method) {
		// stop the vm after "delay" seconds
//		System.out.println("Stopping vm in " + delay + " seconds");
//	}
	
}