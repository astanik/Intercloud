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

import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.ClassificationRegistry;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.rwx4j.container.ResourceInstance;
import de.tu_berlin.cit.rwx4j.plugin.IContainerPlugin;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument.Xwadl;

/**
 * OCCI plugin for XWADL extension.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class OcciXwadlPlugin implements IContainerPlugin {

	protected final static Logger logger = LoggerFactory.getLogger(OcciXwadlPlugin.class);

	/**
	 * Default constructor.
	 */
	public OcciXwadlPlugin() {
		
	}
	
	@Override
	public XwadlDocument extendXwadl(XwadlDocument xwadl, String path, ResourceInstance instance) {
		// lookup for xwadl
		Xwadl resType = xwadl.getXwadl();
		// check summary annotation
		resType = checkSummaryAnnotation(instance.getClass(), resType);
		// add occi classification grammar
		if(instance instanceof Collection || instance instanceof Resource ||
				instance instanceof Link) {
			ClassificationDocument classDoc = ClassificationRegistry.buildClassification(instance);
			resType.addNewGrammars().set(classDoc);
		}
		// return document
		return xwadl;
	}

	private Xwadl checkSummaryAnnotation(Class<? extends Object> resourceClass, Xwadl resType) {
		if (resourceClass.isAnnotationPresent(Summary.class)) {
			String summary = resourceClass.getAnnotation(Summary.class).value();
			resType.addNewDocumentation().setTitle("Summary");
			resType.getDocumentation().setStringValue(summary);
		} else {
			Class<? extends Object> superClass = resourceClass.getSuperclass();
			if (superClass != null) {
				resType = checkSummaryAnnotation(superClass, resType);
			}
		}
		return resType;
	}

}
