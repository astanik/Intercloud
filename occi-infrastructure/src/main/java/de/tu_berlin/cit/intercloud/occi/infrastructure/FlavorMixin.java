package de.tu_berlin.cit.intercloud.occi.infrastructure;

import java.util.Map;

import de.tu_berlin.cit.intercloud.xmpp.rest.Representation;

public class FlavorMixin extends Representation<String> {

	@Override
	public Map<String, String> getOptions() {
		String mediaType = "text/occi";
		
		StringBuilder option = new StringBuilder();
    	option.append("X-OCCI-Attribute: occi.compute.architecture='x86' \n");
    	option.append("X-OCCI-Attribute: occi.compute.memory=2.0 \n");
    	flavor.append("X-OCCI-Attribute: occi.compute.cores=2 \n");
    	return flavor.toString();
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
