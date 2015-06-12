package de.tu_berlin.cit.intercloud.occi.core.classification;

import java.util.List;

public abstract class Category {
	
	final static public String CategorySchema = "http://schema.ogf.org/occi/core#";
	
	final private String schema;
	
	final private String term;
	
	final private String title;
	
	final private List<Attribute> attributes;
	
	protected Category(String schema, String term, String title, List<Attribute> attributes) {
		this.schema = schema;
		this.term = term;
		this.title = title;
		this.attributes = attributes;
	}
	
	public String getSchema() {
		return this.schema;
	}
	
	public String getTerm() {
		return this.term;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public List<Attribute> getAttributes() {
		return this.attributes;
	}
	
}