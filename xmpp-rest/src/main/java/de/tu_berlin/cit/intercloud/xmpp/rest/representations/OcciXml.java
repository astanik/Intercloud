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

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;

import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument.Category;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkDocument.Link;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

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
	
	public List<Link> getLinks() {
		ArrayList<Link> linkList = new ArrayList<Link>();
		Category category = this.catDoc.getCategory();
		// add kind links to array
		if(category.isSetKind()) {
			if(category.getKind().isSetLinks()) {
				Link[] linkArr = category.getKind().getLinks().getLinkArray();
				for(int i = 0; i < linkArr.length; i++) {
					linkList.add(linkArr[i]);
				}
			}
		}
		// add mixin links to array
		CategoryType[] mixins = category.getMixinArray();
		for(int k = 0; k < mixins.length; k++) {
			if(mixins[k].isSetLinks()) {
				Link[] linkArr = mixins[k].getLinks().getLinkArray();
				for(int i = 0; i < linkArr.length; i++) {
					linkList.add(linkArr[i]);
				}
			}
		}
		
		return linkList;
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
