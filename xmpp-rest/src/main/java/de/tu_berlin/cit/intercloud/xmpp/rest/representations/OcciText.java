/**
 * Copyright (C) 2012-2015 TU Berlin. All rights reserved.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

public class OcciText extends Representation {

	public static final String MEDIA_TYPE = "text/occi";
	
	public class OcciLink {
		public String target;
		
	}

	private HashMap<String,String> attributes = new HashMap<String,String>();
	
	public OcciText() {
	}
	
	public OcciText(String text) {
		this.readRepresentation(text);
	}

	public OcciText(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public List<Representation> getTemplates() {
		return null;
	}

	@Override
	public void readRepresentation(String stringRepresentation) {
		// remove all elements
		this.attributes.clear();
		// split string separated by semicolon
		String[] strArray = stringRepresentation.split(";");
		for(int i=0; i<strArray.length; i++) {
			String[] attArray = strArray[i].split("[ :=]+");
			if(attArray.length == 3)
				this.attributes.put(attArray[1], attArray[2]);
		}
	}

	@Override
	public StringBuilder writeRepresentation(StringBuilder representationBuilder) {
		for(Map.Entry<String, String> entry : this.attributes.entrySet()){
			representationBuilder.append("X-OCCI-Attribute:");
			representationBuilder.append(entry.getKey() + "=");
			representationBuilder.append(entry.getValue() + ";");
		}
		return representationBuilder;
	}
	
}
