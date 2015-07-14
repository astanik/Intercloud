/**
 * Copyright 2010-2015 Complex and Distributed IT Systems, TU Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tu_berlin.cit.intercloud.xmpp.rest.representations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
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
