package de.tu_berlin.cit.intercloud.root.services;

import java.util.Collection;

import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;

@Path("/iaas")
public class IaaSCatalog extends ResourceInstance {

	public IaaSCatalog() {
		this.addResource(new ComputeCatalog());
	}

	@XmppMethod(XmppMethod.GET)
	@Produces(value = UriListText.MEDIA_TYPE, serializer = UriListText.class)
	public UriListText getSubResources() {
		UriListText uriList = new UriListText();
		Collection<ResourceInstance> resources = this.getResources();
		for(ResourceInstance res : resources) {
			uriList.addURI(res.getPath());
		}
		return uriList;
	}

}