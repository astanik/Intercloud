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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.XwadlBuilder;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument.ResourceType;

public class OcciXwadlBuilder extends XwadlBuilder {

	protected final static Logger logger = LoggerFactory.getLogger(OcciXwadlBuilder.class);

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
				XwadlBuilder.createMethodXWADL(method, resType.addNewMethod());
		}
		// TODO Actions
		
		logger.info("Finished building xwadl document: " + xwadl.toString());
		return xwadl;
	}

}
