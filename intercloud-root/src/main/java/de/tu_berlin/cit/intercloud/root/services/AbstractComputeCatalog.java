package de.tu_berlin.cit.intercloud.root.services;

import java.util.Collection;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.servicecatalog.ServiceCatalogKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;

@Kind(ServiceCatalogKind.class)
public abstract class AbstractComputeCatalog extends ResourceInstance {

	protected AbstractComputeCatalog() {
		super();
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