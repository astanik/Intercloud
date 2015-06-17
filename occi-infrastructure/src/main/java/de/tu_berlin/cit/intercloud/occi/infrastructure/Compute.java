package de.tu_berlin.cit.intercloud.occi.infrastructure;

import java.net.URISyntaxException;
import java.util.Collection;

import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;

@Path("/compute")
public class Compute extends ResourceInstance {

//	final static public String InfrastructureSchema = AbstractKind.InfrastructureSchema + "compute";
//
//	public Compute(String id, String title, Kind kind, List<Mixin> mixins,
//			List<Link> links, String summary) {
//		super(id, title, kind, mixins, links, summary);
//	}
	
	
	public Compute() {
	}

	@XmppMethod(XmppMethod.GET)
	@Produces(value = UriListText.MEDIA_TYPE, serializer = UriListText.class)
	public UriListText getVMs() {
		UriListText uriList = new UriListText();
		Collection<ResourceInstance> resources = this.getResources();
		for(ResourceInstance res : resources) {
			uriList.addURI(res.getPath());
		}
		return uriList;
	}

	@XmppMethod(XmppMethod.POST)
    @Consumes(value = FlavorMixin.MEDIA_TYPE, serializer = FlavorMixin.class)
    @Produces(value = UriText.MEDIA_TYPE, serializer = UriText.class)
	public UriText createVM(FlavorMixin flavor) {
		// create a virtual machine and return its uri
		VirtualMachine vm = new VirtualMachine(flavor);
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