package de.tu_berlin.cit.intercloud.xmpp.rest;

import java.util.Map;

public abstract class Parameter<T> {

	private String name;
	
	private T def;
	
	private boolean required = false;
	
	private boolean repeating = false;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public T getDefault() {
		return def;
	}

	public void setDefault(T def) {
		this.def = def;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isRepeating() {
		return repeating;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}

	abstract public Map<String, String> getOptions();
	
}
