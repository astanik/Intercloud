package de.tu_berlin.cit.intercloud.occi.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.OcciText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

public class FlavorMixin extends OcciText {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<Representation> getTemplates() {
		List<Representation> list = new ArrayList<Representation>();
		HashMap<String,String> template = new HashMap<String,String>();
		template.put("occi.compute.architecture", "x86");
		template.put("occi.compute.memory", "2.0");
		template.put("occi.compute.cores", "2");
		list.add(new OcciText(template));
		return list;
	}

	
}