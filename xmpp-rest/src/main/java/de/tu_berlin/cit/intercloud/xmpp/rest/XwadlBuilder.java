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

package de.tu_berlin.cit.intercloud.xmpp.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.RequestDocument.Request;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument.ResourceType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResponseDocument.Response;

public class XwadlBuilder {

	protected final static Logger logger = LoggerFactory.getLogger(XwadlBuilder.class);

	public static ResourceTypeDocument build(String path,
			ResourceInstance instance) {
		logger.info("Start building xwadl document");
		// create new document 
		ResourceTypeDocument xwadl = ResourceTypeDocument.Factory.newInstance();
		// set resource path
		ResourceType resType = xwadl.addNewResourceType();
		resType.setPath(path);
		logger.info("resource path=" + path);
		// check summary annotation
		if (instance.getClass().isAnnotationPresent(Summary.class)) {
			String summary = instance.getClass().getAnnotation(Summary.class).value();
			resType.addNewDocumentation().setTitle("Summary");
			resType.getDocumentation().setStringValue(summary);
		}
		// add occi classification grammar
		if(instance.getClass().isAnnotationPresent(Kind.class)) {
			Class<? extends de.tu_berlin.cit.intercloud.occi.core.classification.Kind> kind = instance.getClass().getAnnotation(Kind.class).value();
			try {
				CategoryDocument catDoc = kind.newInstance().getCategoryDocument();
				resType.addNewGrammars().set(catDoc);
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// search methods
		for(java.lang.reflect.Method method : instance.getClass().getMethods()) {
			// create method entry
			if(method.isAnnotationPresent(XmppMethod.class))
				createMethodXWADL(method, resType.addNewMethod());
		}
		// TODO Actions
		
		logger.info("Finished building xwadl document: " + xwadl.toString());
		return xwadl;
	}

	private static void createMethodXWADL(
			java.lang.reflect.Method method,
			de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method xmlMethod) {
		// set method type
		xmlMethod.setType(MethodType.Enum.forString(method.getAnnotation(XmppMethod.class).value()));
//		logger.info("method=" + method.getName() + " has annotation XmppMethod with value=" + xmlMethod.getType().toString());
		
		// add request information
		if(method.isAnnotationPresent(Consumes.class)) {
			Request xmlRequest = xmlMethod.addNewRequest();
			xmlRequest.setMediaType(method.getAnnotation(Consumes.class).value());
			Class<? extends Representation> serializer = method.getAnnotation(Consumes.class).serializer();
			try {
				List<Representation> templates = serializer.newInstance().getTemplates();
				if(templates != null) {
					for(Representation rep : templates) {
						xmlRequest.addNewTemplate().setStringValue(rep.toString());
					}
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// add response information
		if(method.isAnnotationPresent(Produces.class)) {
			Response xmlResponse = xmlMethod.addNewResponse();
			xmlResponse.setMediaType(method.getAnnotation(Produces.class).value());
		}
	}

}
