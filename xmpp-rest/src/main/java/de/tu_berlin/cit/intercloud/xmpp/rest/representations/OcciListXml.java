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

import java.util.List;

import org.apache.xmlbeans.XmlException;

import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryListDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

public class OcciListXml extends Representation {

	public static final String MEDIA_TYPE = "xml/occi";
	
	private CategoryListDocument catDoc = CategoryListDocument.Factory.newInstance();
	
	public OcciListXml() {
	}
	
	public OcciListXml(String text) {
		this.readRepresentation(text);
	}

	public OcciListXml(CategoryListDocument document) {
		this.catDoc = document;
	}
	
	public CategoryListDocument getDocument() {
		return this.catDoc;
	}

	@Override
	public List<Representation> getTemplates() {
		return null;
	}

	@Override
	public void readRepresentation(String stringRepresentation) {
		try {
			this.catDoc = CategoryListDocument.Factory.parse(stringRepresentation);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.catDoc = CategoryListDocument.Factory.newInstance();
		}
	}

	@Override
	public StringBuilder writeRepresentation(StringBuilder representationBuilder) {
		representationBuilder.append(this.catDoc.toString());
		return representationBuilder;
	}
	
}
