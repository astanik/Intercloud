package de.tu_berlin.cit.intercloud.xmpp.rest.representations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

public class UriText extends Representation {

	public static final String MEDIA_TYPE = "'text/uri";

	private URI uri = null;
	
	public UriText() {
	}
	
	public UriText(String text) throws URISyntaxException {
		this.uri = new URI(text);
	}

	@Override
	public List<Representation> getTemplates() {
		return null;
	}

	@Override
	public void readRepresentation(String stringRepresentation) {
		try {
			this.uri = new URI(stringRepresentation);
		} catch (URISyntaxException e) {
			this.uri = null;
			e.printStackTrace();
		}
	}

	@Override
	public StringBuilder writeRepresentation(StringBuilder representationBuilder) {
		representationBuilder.append(this.uri.toASCIIString());
		return representationBuilder;
	}
	
}
