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

package de.tu_berlin.cit.intercloud.gateway.templates;

import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeDocument.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

public class SlaTemplateMixin extends OcciXml {

	@Override
	public List<Representation> getTemplates() {
		List<Representation> list = new ArrayList<Representation>();
		// create compute kind
		CategoryDocument doc = CategoryDocument.Factory.newInstance();
		doc.addNewCategory().addNewKind().setSchema(ComputeKind.ComputeSchema);
		doc.getCategory().getKind().setTerm(ComputeKind.ComputeTerm);
		doc.getCategory().getKind().setTitle(ComputeKind.ComputeTitle);
		// set attributes
		Attribute att = doc.getCategory().getKind().addNewAttribute();
		att.setName("occi.compute.architecture");
		att.setType("Enum");
		att.setStringValue("x64");
		
		att = doc.getCategory().getKind().addNewAttribute();
		att.setName("occi.compute.memory");
		att.setType("Double");
		att.setStringValue("1.0");

		att = doc.getCategory().getKind().addNewAttribute();
		att.setName("occi.compute.cores");
		att.setType("Integer");
		att.setStringValue("2");

//		doc.getCategory().
		// add template
		list.add(new OcciXml(doc));
		
		// return templates
		return list;
	}


}
