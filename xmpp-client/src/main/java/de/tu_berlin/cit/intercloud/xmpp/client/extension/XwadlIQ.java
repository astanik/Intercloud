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

package de.tu_berlin.cit.intercloud.xmpp.client.extension;

import org.jivesoftware.smack.packet.IQ;

import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ActionDocument.Action;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument.ResourceType;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class XwadlIQ extends IQ {

	public static final String ELEMENT = "resource_type";
	public static final String NAMESPACE = "urn:xmpp:rest-xwadl";

	final private ResourceTypeDocument xwadl;
	
	public XwadlIQ(ResourceTypeDocument xwadl) {
		super(ELEMENT, NAMESPACE);
		this.xwadl = xwadl;
	}

	@Override
	protected IQChildElementXmlStringBuilder getIQChildElementBuilder(
			IQChildElementXmlStringBuilder xml) {
		
		// TODO: This builder return an unusable xml string
		// in order to fix this see RestIQ
		ResourceType resource = this.xwadl.getResourceType();
		
		// set resource path
		xml.attribute("path", resource.getPath());
		xml.rightAngleBracket();
		
		// add doc element
		if(resource.isSetDocumentation()) // not allowed
			xml.append(resource.getDocumentation().toString());
		
		// add grammars element
		if(resource.isSetGrammars()) // not allowed
			xml.append(resource.getGrammars().toString());
		
		// add method element
		Method[] methods = resource.getMethodArray();
		for(int i=0; i<methods.length; i++) { // not allowed
			xml.append(methods[i].toString());
		}
		
		// add method element
		Action[] actions = resource.getActionArray();
		for(int i=0; i<actions.length; i++) { // not allowed
			xml.append(actions[i].toString());
		}
		
		// do not close resource type element
		return xml;
	}

	public ResourceTypeDocument getXwadl() {
		return this.xwadl;
	}

}
