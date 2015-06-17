package de.tu_berlin.cit.intercloud.xmpp.rest.representations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

public class UriListText extends Representation {

	public static final String MEDIA_TYPE = "text/uri-list";

	private ArrayList<URI> uris = new ArrayList<URI>();
	
	public UriListText() {
	}
	
	public UriListText(String text) {
		this.readRepresentation(text);
	}

	@Override
	public List<Representation> getTemplates() {
		return null;
	}

	@Override
	public void readRepresentation(String stringRepresentation) {
		// remove all elements
		this.uris.clear();
		// split string separated by semicolon
		String[] strArray = stringRepresentation.split(";");
		for(int i=0; i<strArray.length; i++) {
			try {
				this.uris.add(new URI(strArray[i]));
			} catch (URISyntaxException e) {
				// do nothing and continue reading
				e.printStackTrace();
			}
		}
	}

	@Override
	public StringBuilder writeRepresentation(StringBuilder representationBuilder) {
		for (int i = 0; i < this.uris.size(); i++) {
			representationBuilder.append(this.uris.get(i).toASCIIString() + ";");
		}
		return representationBuilder;
	}

	public void addURI(String path) {
		try {
			this.uris.add(new URI(path));
		} catch (URISyntaxException e) {
			// do nothing and continue reading
			e.printStackTrace();
		}
	}

	
	
	
}
