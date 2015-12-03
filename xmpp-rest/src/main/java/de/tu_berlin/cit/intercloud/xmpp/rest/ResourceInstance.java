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

package de.tu_berlin.cit.intercloud.xmpp.rest;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public abstract class ResourceInstance {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
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
		// update sub resources
		Enumeration<String> subPathes = this.resourceMap.keys();
		while(subPathes.hasMoreElements()) {
			String subPath = subPathes.nextElement();
			ResourceInstance instance = resourceMap.get(subPath);
			// set absolute path in resource
			String absolutePath = this.path + subPath;
			instance.setPath(absolutePath);
			logger.info("Updated the path of ResourceInstance with path=" + subPath
					+ " and absolute path=" + absolutePath);
		}
	}
	
	/**
	 * 
	 * @return Path identifier without a slash
	 */
	public String getPathIdentifier() {
		// split relative path
		String[] elements = this.getPath().split("/");
		if (elements.length > 0) {
			return elements[elements.length - 1];
		} else
			return null;
	}

	public ResourceInstance getParent() {
		return this.parent;
	}

	private void setParent(ResourceInstance newParent) {
		this.parent = newParent;
	}

	public String addResource(ResourceInstance instance, String subPath) {
		// set absolute path in resource
		String absolutePath = this.getPath() + subPath;
		instance.setPath(absolutePath);

		// set parent resource
		instance.setParent(this);

		// add resource to map
		this.resourceMap.put(subPath, instance);
		logger.info("New ResourceInstance had been added to resource map with path=" + subPath
				+ " and absolute path=" + absolutePath);

		// return the absolute path of this resource
		return absolutePath;
	}

	public String addResource(ResourceInstance instance) {
		String newPath = "";
		// generate new relative path
		if (instance.getClass().isAnnotationPresent(Path.class)) {
			newPath = instance.getClass().getAnnotation(Path.class).value();
			logger.info("ResourceInstance has Path annotation=" + newPath);
		} else if (instance.getClass().isAnnotationPresent(PathID.class)) {
			// find unused id
			do {
				newPath = "/" + UUID.randomUUID().toString();
			} while (resourceMap.containsKey(newPath));
			logger.info("ResourceInstance has PathID annotation=" + newPath);
		} else {
			throw new RuntimeException("Failed: XMPP Resource error: "
					+ "The received resource object has no Path annotation.");
		}

		// add resource and return the absolute path of this resource
		return this.addResource(instance, newPath);
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
		logger.info("Lookup ResourceInstance for path=" + resPath);
		String myPath = "";
		// find my resource path
		String[] elements = resPath.split("/");
		if(elements.length < 2)
			return null;
		else
			myPath = "/" + elements[1];

		logger.info("myPath=" + myPath + "  and elements.length=" + elements.length);
		// check if this resource map contains the requested resource
		if(elements.length == 2) {
			logger.info("Lookup ResourceInstance in my resource map with path=" + myPath);
			return this.resourceMap.get(myPath);
		}
		
		// generate relative path
		String relativePath = "";
		for(int i = 2; i < elements.length; i++)
			relativePath = relativePath + "/" + elements[i];
		
		// lookup recursively for resource
		ResourceInstance instance = this.resourceMap.get(myPath);
		if(instance == null)
			return null;
		else 
			return instance.getResource(relativePath);
	}

	public List<ResourceInstance> getResources() {
		return new ArrayList<ResourceInstance>(this.resourceMap.values());
	}

}
