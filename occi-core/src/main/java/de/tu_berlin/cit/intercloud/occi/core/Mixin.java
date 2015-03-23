package de.tu_berlin.cit.intercloud.occi.core;

import java.util.List;

public abstract class Mixin extends Category {

	final private List<Mixin> dependencies;
	
	final private List<Action> actions;

	final private List<Kind> kinds;
	
	private List<Entity> entities;

	protected Mixin(String schema, String term, String title, List<Attribute> attributes, List<Mixin> dependencies, List<Action> actions, List<Kind> kinds, List<Entity> entities) {
		super(schema, term, title, attributes);
		this.dependencies = dependencies;
		this.actions = actions;
		this.kinds = kinds;
		this.entities = entities;
	}

	public List<Mixin> getDependencies() {
		return this.dependencies;
	}
	
	public List<Action> getActions() {
		return this.actions;
	}
	
	public List<Kind> getKinds() {
		return this.kinds;
	}
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
}
