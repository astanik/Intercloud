package de.tu_berlin.cit.intercloud.occi.core;

import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.classification.Mixin;

public class Resource extends Entity {

	private List<Link> links;
	
	private String summary;
	
	public Resource(String id, String title, Kind kind, List<Mixin> mixins, List<Link> links, String summary) {
		super(id, title, kind, mixins);
		this.links = links;
		this.summary = summary;
	}
	
	public List<Link> getLinks() {
		return this.links;
	}
	
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public String getSummary() {
		return this.summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
