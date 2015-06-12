package de.tu_berlin.cit.intercloud.occi.core.classification;

import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.Entity;

public abstract class Kind extends Category {

	final private Kind parent;
	
	final private List<Action> actions;
	
	final private List<Entity> entities;
	
	protected Kind(String schema, String term, String title, List<Attribute> attributes, Kind parent, List<Action> actions, List<Entity> entities) {
		super(schema, term, title, attributes);
		this.parent = parent;
		this.actions = actions;
		this.entities = entities;
	}

	protected Kind(String schema, String term, String title, List<Attribute> attributes, List<Action> actions, List<Entity> entities) {
		this(schema, term, title, attributes, null, actions, entities);
	}
	
	public Kind getParent() {
		return this.parent;
	}
	
	public List<Action> getAction() {
		return this.actions;
	}
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
}
