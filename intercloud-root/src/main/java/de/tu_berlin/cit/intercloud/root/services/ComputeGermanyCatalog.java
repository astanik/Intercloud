package de.tu_berlin.cit.intercloud.root.services;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;

@Path("/de")
public class ComputeGermanyCatalog extends AbstractComputeCatalog {

	public ComputeGermanyCatalog() {
		this.addResource(new ComputeBerlinCatalog());
		this.addResource(new ComputeNiedersachsenCatalog());
		this.addResource(new ComputeMecklenburgVorpommernCatalog());
	}

}