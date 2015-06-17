package de.tu_berlin.cit.intercloud.xmpp.rest.representations;

import java.util.List;

public abstract class Representation {

	public abstract List<Representation> getTemplates();
	
	public abstract void readRepresentation(String stringRepresentation);
	
	public abstract StringBuilder writeRepresentation(StringBuilder representationBuilder);

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder = this.writeRepresentation(builder);
		return builder.toString();
	}
}
