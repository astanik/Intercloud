package de.tu_berlin.cit.intercloud.occi.core;

public abstract class Attribute {

	final private String name;
	
	final private String type;
	
	final private Boolean mutable;
	
	final private Boolean required;
	
	final private String pattern;
	
	final private String def;
	
	final private String descriptiopn;
	
	protected Attribute(String name, String type, Boolean mutable, Boolean required, String pattern, String def, String description) {
		this.name = name;
		this.type = type;
		this.mutable = mutable;
		this.required = required;
		this.pattern = pattern;
		this.def = def;
		this.descriptiopn = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public Boolean getMutable() {
		return this.mutable;
	}
	
	public Boolean getRequired() {
		return this.required;
	}
	
	public String getPattern() {
		return this.pattern;
	}
	
	public String getDefault() {
		return this.def;
	}
	
	public String getDescription() {
		return this.descriptiopn;
	}

}
