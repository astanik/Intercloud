package de.tu_berlin.cit.intercloud.occi.core.classification;

import java.util.List;

public abstract class Action extends Category {

	protected Action(String schema, String term, String title, List<Attribute> attributes, List<Action> dependencies) {
		super(schema, term, title, attributes);
	}

	public abstract void invoke();
	
}
