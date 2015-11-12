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

package de.tu_berlin.cit.intercloud.occi.core;

import java.util.List;

import org.apache.xmlbeans.XmlException;

import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class OcciXml extends Representation {

	public static final String MEDIA_TYPE = "xml/occi";
	
	private CategoryDocument catDoc = CategoryDocument.Factory.newInstance();
	
	public OcciXml() {
	}
	
	public OcciXml(String text) {
		this.readRepresentation(text);
	}

	public OcciXml(CategoryDocument document) {
		this.catDoc = document;
	}

	public CategoryDocument getDocument() {
		return this.catDoc;
	}
	
	public CategoryType getKind() {
		if(this.catDoc.getCategory().isSetKind())
			return this.catDoc.getCategory().getKind();
		else
			return null;
	}

	public CategoryType[] getMixins() {
		return this.catDoc.getCategory().getMixinArray();
	}

	public LinkType[] getLinks() {
		return this.catDoc.getCategory().getLinkArray();
	}
	
	public void removeLinks() {
		for(int i = this.catDoc.getCategory().sizeOfLinkArray() - 1; i >= 0; i--)
			this.catDoc.getCategory().removeLink(i);
	}

	public void addLink(LinkType linkTypeRepresentation) {
		this.catDoc.getCategory().addNewLink().set(linkTypeRepresentation);
	}
	
	public OcciXml getCopy() {
		return new OcciXml(this.catDoc.toString());
	}
	
	@Override
	public List<Representation> getTemplates() {
		return null;
	}

	@Override
	public void readRepresentation(String stringRepresentation) {
		try {
			this.catDoc = CategoryDocument.Factory.parse(stringRepresentation);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.catDoc = CategoryDocument.Factory.newInstance();
		}
	}

	@Override
	public StringBuilder writeRepresentation(StringBuilder representationBuilder) {
		representationBuilder.append(this.catDoc.toString());
		return representationBuilder;
	}

}
