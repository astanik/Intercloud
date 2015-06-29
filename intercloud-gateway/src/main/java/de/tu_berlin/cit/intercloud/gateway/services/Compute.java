package de.tu_berlin.cit.intercloud.gateway.services;

import java.net.URISyntaxException;

import de.tu_berlin.cit.intercloud.gateway.templates.FlavorMixin;
import de.tu_berlin.cit.intercloud.occi.core.OcciText;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.CollectionResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;

@Path("/compute")
@Summary("This resource allows for manage compute instances, e.g. creating virtual machines.")
@Kind(ComputeKind.class)
public class Compute extends CollectionResourceInstance {

	public Compute() {
		super();
	}

	@XmppMethod(XmppMethod.POST)
    @Consumes(value = OcciText.MEDIA_TYPE, serializer = FlavorMixin.class)
    @Produces(value = UriText.MEDIA_TYPE, serializer = UriText.class)
	public UriText createVM(FlavorMixin flavor) {
		// create a virtual machine and return its uri
		ComputeInstance vm = new ComputeInstance(flavor);
		String path = this.addResource(vm);
		try {
			UriText uri = new UriText(path);
			return uri;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new UriText(); 
		}
	}
	
}