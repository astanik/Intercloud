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


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.incarnation.ClassificationRegistry;
import de.tu_berlin.cit.rwx4j.container.ResourceContainer;
import de.tu_berlin.cit.rwx4j.container.ResourceInstance;
import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.rest.ParameterDocument.Parameter;
import de.tu_berlin.cit.rwx4j.rest.RestDocument;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument;


/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class OcciContainer extends ResourceContainer {

	
	public OcciContainer(XmppURI uri) {
		super(uri);
	}
	
	@Override
	public XwadlDocument getXWADL(String path) {
		logger.info("An XWADL is requested for path=" + path);
		// search instance
		ResourceInstance instance = this.getResource(path);
		if(instance == null)
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Resource not found");
		
		return OcciXwadlBuilder.build(path, instance);
	}

	public List<String> getSupportedTypes() {
		ArrayList<String> list = new ArrayList<String>();
		List<ResourceInstance> resources = this.getResources();
		for(int i=0; i<resources.size(); i++) {
			list.addAll(ClassificationRegistry.buildTypeList(resources.get(i)));
		}
		return list;
	}

	@Override
	public RestDocument execute(RestDocument xmlRequest) {
		logger.info("An invocation is requested with xml=" + xmlRequest.toString());
		// create response document
		RestDocument xmlResponse = (RestDocument) xmlRequest.copy();
		String path = xmlRequest.getRest().getPath();
		// search instance
		ResourceInstance instance = this.getResource(path);
		if(instance == null)
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Resource not found");
		
		// invoke method
		if(xmlRequest.getRest().isSetMethod()) {
			try {
				this.invokeMethod(xmlResponse.getRest().getMethod(), instance);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("Failed: ResourceContainer: "
						+ e.getMessage());
			}
			// remove request part
			if(xmlResponse.getRest().getMethod().isSetRequest()) {
				xmlResponse.getRest().getMethod().unsetRequest();
			}
		}
		
		// invoke action
		if(xmlRequest.getRest().isSetAction()) {
			this.invokeAction(xmlResponse.getRest().getAction(), instance);
			// remove request part
			Parameter[] params = xmlResponse.getRest().getAction().getParameterArray();
			for(int i = params.length; i >= 0; i--) {
				xmlResponse.getRest().getAction().removeParameter(i);
			}
		}
		
		logger.info("An invocation was performed and returned is xml=" + xmlResponse.toString());
		return xmlResponse;
	}

	
}
