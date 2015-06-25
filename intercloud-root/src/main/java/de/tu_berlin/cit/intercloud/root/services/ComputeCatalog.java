package de.tu_berlin.cit.intercloud.root.services;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;

@Path("/compute")
public class ComputeCatalog extends AbstractComputeCatalog {

	public ComputeCatalog() {
		this.addResource(new ComputeGermanyCatalog());
	}

}