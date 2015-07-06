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

package de.tu_berlin.cit.intercloud.occi.core;


import java.lang.reflect.InvocationTargetException;

import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ParameterDocument.Parameter;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;


public class OcciContainer extends ResourceContainer {

	
	public OcciContainer(XmppURI uri) {
		super(uri);
	}
	
	@Override
	public ResourceTypeDocument getXWADL(String path) {
		logger.info("An XWADL is requested for path=" + path);
		// search instance
		ResourceInstance instance = this.getResource(path);
		if(instance == null)
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Resource not found");
		
		return OcciXwadlBuilder.build(path, instance);
	}

	@Override
	public ResourceDocument execute(ResourceDocument xmlRequest) {
		logger.info("An invocation is requested with xml=" + xmlRequest.toString());
		// create response document
		ResourceDocument xmlResponse = (ResourceDocument) xmlRequest.copy();
		String path = xmlRequest.getResource().getPath();
		// search instance
		ResourceInstance instance = this.getResource(path);
		if(instance == null)
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Resource not found");
		
		// invoke method
		if(xmlRequest.getResource().isSetMethod()) {
			try {
				this.invokeMethod(xmlResponse.getResource().getMethod(), instance);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("Failed: ResourceContainer: "
						+ e.getMessage());
			}
			// remove request part
			if(xmlResponse.getResource().getMethod().isSetRequest()) {
				xmlResponse.getResource().getMethod().unsetRequest();
			}
		}
		
		// invoke action
		if(xmlRequest.getResource().isSetAction()) {
			this.invokeAction(xmlResponse.getResource().getAction(), instance);
			// remove request part
			Parameter[] params = xmlResponse.getResource().getAction().getParameterArray();
			for(int i = params.length; i >= 0; i--) {
				xmlResponse.getResource().getAction().removeParameter(i);
			}
		}
		
		logger.info("An invocation was performed and returned is xml=" + xmlResponse.toString());
		return xmlResponse;
	}

	
}
