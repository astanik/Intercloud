package de.tu_berlin.cit.intercloud.gateway.templates;

import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeDocument.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

public class FlavorMixin extends OcciXml {

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

		// add template
		list.add(new OcciXml(doc));
		
		// return templates
		return list;
	}


}
