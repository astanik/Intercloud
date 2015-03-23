package de.tu_berlin.cit.intercloud.occi.infrastructure;

import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.Kind;
import de.tu_berlin.cit.intercloud.occi.core.Link;
import de.tu_berlin.cit.intercloud.occi.core.Mixin;
import de.tu_berlin.cit.intercloud.occi.core.Resource;

public class Compute extends Resource {

	final static public String InfrastructureSchema = Infrastructure.InfrastructureSchema + "compute";

	public Compute(String id, String title, Kind kind, List<Mixin> mixins,
			List<Link> links, String summary) {
		super(id, title, kind, mixins, links, summary);
	}
	
}