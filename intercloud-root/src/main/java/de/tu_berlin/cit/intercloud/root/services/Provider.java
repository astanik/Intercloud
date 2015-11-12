package de.tu_berlin.cit.intercloud.root.services;

import de.tu_berlin.cit.intercloud.occi.core.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.servicecatalog.ProviderLink;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Summary("This resource refers to the providers exchange service.")
@Classification(links = {ProviderLink.class})
public class Provider extends Link {

	public Provider(LinkType linkRepresentation) {
		super(linkRepresentation);
		// TODO Auto-generated constructor stub
	}

}
