package de.tu_berlin.cit.intercloud.occi.text;

import de.tu_berlin.cit.intercloud.occi.core.Attribute;

public abstract class TextAttribute extends Attribute {

	final static public String PREFIX = "X-OCCI-Attribute: ";

	protected TextAttribute(String name, String type, Boolean mutable,
			Boolean required, String pattern, String def, String description) {
		super(name, type, mutable, required, pattern, def, description);
	}

	protected abstract String getValueAsString();
	
	protected abstract void parseValue(String value);
	
	@Override
	public String toString() {
		return PREFIX + this.getName() + "=" + getValueAsString();
	}

}
