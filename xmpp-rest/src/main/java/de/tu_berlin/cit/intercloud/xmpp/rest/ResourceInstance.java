package de.tu_berlin.cit.intercloud.xmpp.rest;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;

public abstract class ResourceInstance {

	private final ConcurrentHashMap<String, ResourceInstance> resourceMap = new ConcurrentHashMap<String, ResourceInstance>();

	private String path = "";

	private ResourceInstance parent = null;

	protected ResourceInstance() {
	}

	protected ResourceInstance(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}

	private void setPath(String newPath) {
		this.path = newPath;
	}

	public ResourceInstance getParent() {
		return this.parent;
	}

	private void setParent(ResourceInstance newParent) {
		this.parent = newParent;
	}

	public String addResource(ResourceInstance instance) {
		String newPath = "";
		// generate new relative path
		if (instance.getClass().isAnnotationPresent(Path.class)) {
			newPath = instance.getClass().getAnnotation(Path.class).value();
		} else if (instance.getClass().isAnnotationPresent(PathID.class)) {
			// find unused id
			do {
				newPath = "/" + UUID.randomUUID().toString();
			} while (resourceMap.containsKey(newPath));
		} else {
			throw new RuntimeException("Failed: XMPP Resource error: "
					+ "The recified resource object has no Path annotation.");
		}

		// set absolute path in resource
		String absolutePath = this.getPath() + newPath;
		instance.setPath(absolutePath);

		// set parent resource
		instance.setParent(this);

		// add resource to map
		this.resourceMap.put(newPath, instance);

		// return the absolute path of this resource
		return absolutePath;
	}

	public boolean removeResource(ResourceInstance instance) {
		// find relative path
		String[] elements = instance.getPath().split("/");
		if (elements.length > 0) {
			String relPath = "/" + elements[elements.length - 1];
			ResourceInstance res = this.resourceMap.remove(relPath);
			if (res == null) {
				return false;
			} else {
				res.setParent(null);
			}
			return true;
		} else
			return false;
	}
	
	public ResourceInstance getResource(String resPath) {
		String myPath = "";
		// find my resource path
		String[] elements = resPath.split("/");
		if(elements.length < 1)
			return null;
		else
			myPath = "/" + elements[0];

		// check if this resource map contains the requested resource
		if(elements.length == 1)
			return this.resourceMap.get(myPath);
		
		// generate relative path
		String relativePath = "";
		for(int i = 1; i < elements.length; i++)
			relativePath = relativePath + "/" + elements[i];
		
		// lookup recursively for resource
		ResourceInstance instance = this.resourceMap.get(myPath);
		if(instance == null)
			return null;
		else 
			return instance.getResource(relativePath);
	}

	public Collection<ResourceInstance> getResources() {
		return this.resourceMap.values();
	}

}
