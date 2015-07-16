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
package de.tu_berlin.cit.intercloud.gateway.services;

import de.tu_berlin.cit.intercloud.occi.core.Collection;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.platform.ApplicationKind;
import de.tu_berlin.cit.intercloud.occi.platform.ComponentLink;
import de.tu_berlin.cit.intercloud.occi.platform.DatabaseLinkMixin;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 * @author Daniel Thilo Schroeder <daniel.schroeder@mailbox.tu-berlin.de>
 */
@Path("/application")
@Summary("This resources represent the Applications.")
@Classification(kind = ApplicationKind.class,
				mixins = {DatabaseLinkMixin.class},
				links = {ComponentLink.class})
public class Application extends Collection{

	public Application(){
		super();
	}
}
