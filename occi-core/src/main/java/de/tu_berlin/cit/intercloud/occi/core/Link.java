package de.tu_berlin.cit.intercloud.occi.core;

import java.util.List;

public class Link extends Entity {

	private Resource source;
	
	private String target;
	
	public Link(String id, String title, Kind kind, List<Mixin> mixins, Resource source, String target) {
		super(id, title, kind, mixins);
		this.source = source;
		this.target = target;
	}
	
	public Resource getSource() {
		return this.source;
	}
	
	public void setSource(Resource source) {
		this.source = source;
	}
	
	public String getTarget() {
		return this.target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
}
