package de.tu_berlin.cit.intercloud.xmpp.rest.representations;

import java.util.List;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

public class PlainText extends Representation {


	public static final String MEDIA_TYPE = "text/plain";

	private String text;
	
	public PlainText() {
		this("");
	}
	
	public PlainText(String text) {
		this.text = text;
	}

	@Override
	public List<Representation> getTemplates() {
		return null;
	}

	@Override
	public void readRepresentation(String stringRepresentation) {
		this.text = stringRepresentation;
	}

	@Override
	public StringBuilder writeRepresentation(StringBuilder representationBuilder) {
		representationBuilder.append(this.text);
		return representationBuilder;
	}

	
	
	
}
