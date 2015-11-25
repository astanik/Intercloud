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

package de.tu_berlin.cit.intercloud.xmpp.rest.client;

import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.PlainText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType.Enum;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ResourceClient {

	private final ResourceTypeDocument doc;

	public ResourceClient(ResourceTypeDocument doc) {
		this.doc = doc;
	}

	public List<Method> getMethods(Enum type) {
		Method[] xmlMethods = this.doc.getResourceType().getMethodArray();
		ArrayList<Method> list = new ArrayList<Method>();
		for (int i = 0; i < xmlMethods.length; i++)
			if (xmlMethods[i].getType().toString().equals(type.toString()))
				list.add(xmlMethods[i]);

		return list;
	}

	public Method getMethod(Enum type, String requestMediaType,
			String responseMediaType) {
		boolean requestMatchs = false;
		boolean responseMatchs = false;
		// get methods of type
		List<Method> list = this.getMethods(type);
		for (Method method : list) {
			// check request
			if (method.isSetRequest() && requestMediaType != null) {
				String mediaType = method.getRequest().getMediaType();
				if (mediaType.equals(requestMediaType)) {
					requestMatchs = true;
				}
			} else if (!method.isSetRequest() && requestMediaType == null) {
				requestMatchs = true;
			}
			// check response
			if (method.isSetResponse() && responseMediaType != null) {
				String mediaType = method.getResponse().getMediaType();
				if (mediaType.equals(responseMediaType)) {
					responseMatchs = true;
				}
			} else if (!method.isSetRequest() && requestMediaType == null) {
				responseMatchs = true;
			}
			// check matching
			if (requestMatchs && responseMatchs) {
				return method;
			} else {
				requestMatchs = false;
				responseMatchs = false;
			}
		}

		// return null if no matching method has been found
		return null;
	}

	public MethodInvocation buildMethodInvocation(Method method) {
		ResourceDocument resourceDoc = createBasicResourceDocument();
		return new MethodInvocation(resourceDoc, method);
	}

	protected ResourceDocument createBasicResourceDocument() {
		ResourceDocument resourceDoc = ResourceDocument.Factory.newInstance();
		resourceDoc.addNewResource().setPath(
				this.doc.getResourceType().getPath());
		return resourceDoc;
	}

	public Class<? extends Representation> getRequestRepresentationClass(
			Method method) {
		if (method.isSetRequest()) {
			String mediaType = method.getRequest().getMediaType();
			if (mediaType.equals(PlainText.MEDIA_TYPE)) {
				return PlainText.class;
			} else if (mediaType.equals(UriText.MEDIA_TYPE)) {
				return UriText.class;
			} else if (mediaType.equals(UriListText.MEDIA_TYPE)) {
				return UriListText.class;
			}
		}
		return null;
	}

	public Representation getRequestRepresentation(Method method) {
		Class<? extends Representation> repClass = getRequestRepresentationClass(method);
		Representation rep = null;
		try {
			if (repClass != null)
				rep = repClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rep;
	}

	public List<Representation> getRequestTemplates(Method method) {
		List<Representation> templates = new ArrayList<Representation>();
		if (method.isSetRequest()) {
			String[] tmpl = method.getRequest().getTemplateArray();
			for (int i = 0; i < tmpl.length; i++) {
				Representation rep = getRequestRepresentation(method);
				rep.readRepresentation(tmpl[i]);
				templates.add(rep);
			}
		}
		return templates;
	}

	public ResourceTypeDocument getResourceTypeDocument() {
		return doc;
	}

}
