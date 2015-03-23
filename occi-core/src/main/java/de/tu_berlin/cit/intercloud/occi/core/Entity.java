package de.tu_berlin.cit.intercloud.occi.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
	
	final private String id;
	
	private String title;
	
	final private Kind kind;
	
	private List<Mixin> mixins;
	
	protected Entity(String id, String title, Kind kind, List<Mixin> mixins) {
		this.id = id;
		this.title = title;
		this.kind = kind;
		this.mixins = mixins;
	}
	
	protected Entity(String id, Kind kind) {
		this(id, "", kind, new ArrayList<Mixin>());
	}

	public String getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Kind getKind() {
		return this.kind;
	}
	
	public List<Mixin> getMixins() {
		return this.mixins;
	}
	
}