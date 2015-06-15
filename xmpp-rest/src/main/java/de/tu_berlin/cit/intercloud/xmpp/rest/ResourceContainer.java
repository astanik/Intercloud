package de.tu_berlin.cit.intercloud.xmpp.rest;

import java.util.concurrent.ConcurrentHashMap;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument.ResourceType;


public class ResourceContainer {

	private final ConcurrentHashMap<String, ResourceInstance> resourceMap;
	
	public ResourceContainer() {
		this.resourceMap = new ConcurrentHashMap<String, ResourceInstance>();
	}
	
	public void addResource(ResourceInstance resource) {
		String path = resource.getClass().getAnnotation(Path.class).value();
		this.resourceMap.put(path, resource);
	}

	public ResourceTypeDocument getXWADL(String path) {
		ResourceTypeDocument xwadl = ResourceTypeDocument.Factory.newInstance();
		ResourceType resType = xwadl.addNewResourceType();
		resType.setPath(path);
		ResourceInstance instance = this.resourceMap.get(path);
		if(instance == null)
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Resource not found");
		
		instance.getClass()
		
		// TODO Auto-generated method stub
		return xwadl;
	}

	public ResourceDocument execute(ResourceDocument xmlRequest) {
		ResourceDocument xmlResponse = (ResourceDocument) xmlRequest.copy();
		// TODO Auto-generated method stub
		return xmlResponse;
	}

	
	
	
}
