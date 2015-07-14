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


import java.lang.reflect.InvocationTargetException;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ActionDocument.Action;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ParameterDocument.Parameter;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;


/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ResourceContainer extends ResourceInstance {

	
	public ResourceContainer(XmppURI uri) {
		super(uri.toString());
	}
	
	public ResourceTypeDocument getXWADL(String path) {
		logger.info("An XWADL is requested for path=" + path);
		// search instance
		ResourceInstance instance = this.getResource(path);
		if(instance == null)
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Resource not found");
		
		return XwadlBuilder.build(path, instance);
	}

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
		
//		logger.info("An invocation was performed and returned is xml=" + xmlResponse.toString());
		return xmlResponse;
	}

	protected void invokeMethod(Method xmlMethod, ResourceInstance instance) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		java.lang.reflect.Method method = this.searchMethod(xmlMethod, instance);
		if(method == null) {
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Method not found");
		}
		
		// create representations
		Representation input = null;
		if(method.isAnnotationPresent(Consumes.class)) {
			input = method.getAnnotation(Consumes.class).serializer().newInstance();
			input.readRepresentation(xmlMethod.getRequest().getRepresentation());
		}

		// with or without a response
		if(method.isAnnotationPresent(Produces.class)) {
			Representation output = null;
			if(input == null)
				output = (Representation) method.invoke(instance, new Object[] {});
			else
				output = (Representation) method.invoke(instance, input);
		
			StringBuilder builder = new StringBuilder();
			builder = output.writeRepresentation(builder);
			xmlMethod.getResponse().setRepresentation(builder.toString());
		} else {
			if(input == null)
				method.invoke(instance, new Object[] {});
			else
				method.invoke(instance, input);
		}
		
	}

	protected java.lang.reflect.Method searchMethod(Method xmlMethod,
			ResourceInstance instance) {
		String methodType = xmlMethod.getType().toString();
		// search methods
		for(java.lang.reflect.Method method : instance.getClass().getMethods()) {
			// is method of searched type
			if(method.isAnnotationPresent(XmppMethod.class))
				if(methodType.equals(method.getAnnotation(XmppMethod.class).value()))
					if(this.isMethodCorrectAnnotated(xmlMethod, method))
						return method;
		}
		return null;
	}

	protected boolean isMethodCorrectAnnotated(Method xmlMethod,
			java.lang.reflect.Method method) {
		
		// if there is no input
		if(!xmlMethod.isSetRequest() && !method.isAnnotationPresent(Consumes.class)) {
			// if no output
			if(!xmlMethod.isSetResponse() && !method.isAnnotationPresent(Produces.class)) {
				return true;
			} // if both have output
			else if(xmlMethod.isSetResponse() && method.isAnnotationPresent(Produces.class)) {
				if(xmlMethod.getResponse().getMediaType().equals(method.getAnnotation(Produces.class).value()))
					return true;
			}
		} // if both have input
		else if(xmlMethod.isSetRequest() && method.isAnnotationPresent(Consumes.class)) {
			// if both have the same media type
			if(xmlMethod.getRequest().getMediaType().equals(method.getAnnotation(Consumes.class).value())) {
				// if no output
				if(!xmlMethod.isSetResponse() && !method.isAnnotationPresent(Produces.class)) {
					return true;
				} // if both have output
				else if(xmlMethod.isSetResponse() && method.isAnnotationPresent(Produces.class)) {
					if(xmlMethod.getResponse().getMediaType().equals(method.getAnnotation(Produces.class).value()))
						return true;
				}
			}
		} 
		
		// in all other cases
		return false;
	}

	protected void invokeAction(Action xmlAction, ResourceInstance instance) {
		// TODO Auto-generated method stub
		
	}

	
	
}
