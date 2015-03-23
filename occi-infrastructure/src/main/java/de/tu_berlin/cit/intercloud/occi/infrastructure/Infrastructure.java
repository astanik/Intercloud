package de.tu_berlin.cit.intercloud.occi.infrastructure;

import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.Action;
import de.tu_berlin.cit.intercloud.occi.core.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.Entity;
import de.tu_berlin.cit.intercloud.occi.core.Kind;

public abstract class Infrastructure extends Kind {

	final static public String InfrastructureSchema = "http://schema.ogf.org/occi/infrastructure#";

	protected Infrastructure(String schema, String term, String title,
			List<Attribute> attributes, Kind parent, List<Action> actions,
			List<Entity> entities) {
		super(schema, term, title, attributes, parent, actions, entities);
	}

	protected Infrastructure(String schema, String term, String title,
			List<Attribute> attributes, List<Action> actions,
			List<Entity> entities) {
		super(schema, term, title, attributes, actions, entities);
	}

}