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

import java.net.URISyntaxException;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.sla.AgreementKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Summary("This resource allows for manage service level agreements.")
@Classification(kind = AgreementKind.class)
public class AgreementInstance extends Resource {


	public AgreementInstance(OcciXml agreementXml) {
		super(agreementXml);
	}

	public AgreementInstance() {
		// TODO Auto-generated constructor stub
	}

	@XmppMethod(XmppMethod.PUT)
    @Consumes(value = OcciXml.MEDIA_TYPE, serializer = OcciXml.class)
    @Produces(value = UriText.MEDIA_TYPE, serializer = UriText.class)
	public UriText createGuaranteeTerm(OcciXml agreementXml) {
		// create a guarantee term link and return its uri
		AgreementInstance agreement = new AgreementInstance(agreementXml);
		String path = this.addResource(agreement);
		try {
			UriText uri = new UriText(path);
			return uri;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new UriText(); 
		}
	}
	
}
