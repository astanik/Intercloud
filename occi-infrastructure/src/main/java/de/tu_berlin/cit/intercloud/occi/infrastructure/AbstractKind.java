package de.tu_berlin.cit.intercloud.occi.infrastructure;

import java.net.URI;
import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.Entity;
import de.tu_berlin.cit.intercloud.occi.core.classification.Action;
import de.tu_berlin.cit.intercloud.occi.core.classification.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;

public abstract class AbstractKind extends Kind {

	final static public String InfrastructureSchema = "http://schema.ogf.org/occi/infrastructure#";

	protected AbstractKind(URI schema, String term, String title,
			List<Attribute> attributes, Kind parent, List<Action> actions,
			List<Entity> entities) {
		super(schema, term, title, attributes, parent, actions, entities);
	}

	protected AbstractKind(URI schema, String term, String title,
			List<Attribute> attributes, List<Action> actions,
			List<Entity> entities) {
		super(schema, term, title, attributes, actions, entities);
	}

}