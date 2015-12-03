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

package de.tu_berlin.cit.intercloud.sla;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.Link;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.RepresentationBuilder;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.sla.AgreementKind;
import de.tu_berlin.cit.intercloud.sla.links.AvailabilityGuaranteeTerm;
import de.tu_berlin.cit.intercloud.sla.mixins.AvailabilityMixin;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Summary("This resource represents a particular service level agreements.")
@Classification(kind = AgreementKind.class)
public class AgreementInstance extends Resource {


	public AgreementInstance(OcciXml agreementXml) {
		super(agreementXml);
	}

	public AgreementInstance() {
		// TODO Auto-generated constructor stub
	}

	@XmppMethod(value = XmppMethod.PUT, documentation = "This method adds a set of guarantee terms as link resources.")
    @Consumes(value = OcciXml.MEDIA_TYPE, serializer = OcciXml.class)
    @Produces(value = UriListText.MEDIA_TYPE, serializer = UriListText.class)
	public UriListText createTerm(OcciXml agreementXml) {
		UriListText uriList = new UriListText();
		LinkType[] links = agreementXml.getLinks();
		for(LinkType link : links) {
			uriList.addURI(this.createTerm(link));
		}
		return uriList;
	}
	
	private String createTerm(LinkType link) {
		Link linkInstance = null;
		// create a term link and return its uri
		try {
			AvailabilityMixin availabilityMixin = RepresentationBuilder.buildLinkRepresentation(link, new AvailabilityMixin());
			if(availabilityMixin.slo != null)
				linkInstance = new AvailabilityGuaranteeTerm(link);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(linkInstance == null)
			return "";
		else
			return this.addResource(linkInstance);
	}
}
