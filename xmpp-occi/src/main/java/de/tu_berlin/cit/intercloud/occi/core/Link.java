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

import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class Link extends ResourceInstance {

	private final LinkType linkRepresentation;
	
	public Link(LinkType linkRepresentation) {
		super();
		this.linkRepresentation = linkRepresentation;
	}
	
	public Link() {
		this(LinkType.Factory.newInstance());
	}

	@XmppMethod(XmppMethod.GET)
	@Produces(value = OcciXml.MEDIA_TYPE, serializer = OcciXml.class)
	public OcciXml getRepresentation() {
		CategoryDocument doc = CategoryDocument.Factory.newInstance();
		doc.addNewCategory().addNewLink().set(linkRepresentation);
		return new OcciXml(doc);
	}
	
	public LinkType getLinkTypeRepresentation() {
		return this.linkRepresentation;
	}
	
	@XmppMethod(XmppMethod.DELETE)
	public void deleteResource() {
		this.getParent().removeResource(this);
	}

	public ResourceInstance getSource() {
		return this.getParent();
	}
	
	public String getTarget() {
		if(this.linkRepresentation.isSetTarget())
			return this.linkRepresentation.getTarget();
		else
			return null;
	}
	
}
