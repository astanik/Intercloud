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

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.PlainText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.RequestDocument.Request;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class MethodInvocation {

	private final ResourceDocument doc;
	
	public MethodInvocation(ResourceDocument resourceDoc, de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method method) {
		this.doc = resourceDoc;
		// set method type
		this.doc.getResource().addNewMethod().setType(MethodType.Enum.forString(method.getType().toString()));
		// set request
		if(method.isSetRequest()) {
			this.doc.getResource().getMethod().addNewRequest().setMediaType(method.getRequest().getMediaType());
		}
		// set response
		if(method.isSetResponse()) {
			this.doc.getResource().getMethod().addNewResponse().setMediaType(method.getResponse().getMediaType());
		}
	}
	
	public ResourceDocument getXmlDocument() {
		return this.doc;
	}

	public void setRequestRepresentation(String representation) {
		if (this.doc.getResource().getMethod().isSetRequest()) {
			Request request = this.doc.getResource().getMethod().getRequest();
			request.setRepresentation(representation);
		}
	}

	public void setRequestRepresentation(Representation rep) {
		StringBuilder builder = new StringBuilder();
		builder = rep.writeRepresentation(builder);
		setRequestRepresentation(builder.toString());
	}
	
	public Class<? extends Representation> getResponseRepresentationClass(Method method) {
		if(method.isSetResponse()) {
			String mediaType = method.getResponse().getMediaType();
			if(mediaType.equals(PlainText.MEDIA_TYPE)) {
				return PlainText.class;
			} else if(mediaType.equals(UriText.MEDIA_TYPE)) {
				return UriText.class;
			} else if(mediaType.equals(UriListText.MEDIA_TYPE)) {
				return UriListText.class;
			}
		}
		return null;
	}
	
	public Representation getResponseRepresentation(Method method) {
		Class<? extends Representation> repClass = getResponseRepresentationClass(method);
		Representation rep = null;
		try {
			if(repClass != null) {
				rep = repClass.newInstance();
				if(method.getResponse().isSetRepresentation()) {
					rep.readRepresentation(method.getResponse().getRepresentation());
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rep;
	}

}
