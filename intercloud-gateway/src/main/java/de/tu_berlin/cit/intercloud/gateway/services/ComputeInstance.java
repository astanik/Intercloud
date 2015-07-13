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

import java.util.UUID;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Parameter;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Result;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppAction;

@PathID
@Summary("This resource allows for manage a particular virtual machine.")
@Classification(kind = ComputeKind.class)
public class ComputeInstance extends Resource {

	private static UUID TemplateID = UUID.fromString("f509099b-0da9-4f96-8fe6-7b20f6614381");

	public ComputeInstance(OcciXml rep) {
		super(rep);
	}


//	@Action("start")
//	@Result()
	public Boolean start(String message) {
		// starting vm
		System.out.println("Stating vm with message: " + message);
		return true;
	}
	
	@XmppAction(value = "stop", documentation = "Stop this virtual machine")
	@Result(documentation = "Returns true if the vm has been stopped successfully")
	public Boolean stop( @Parameter(value = "method", documentation = "The method used for stopping this vm") String method) {
		// stop the vm after "delay" seconds
		System.out.println("Stopping vm with method: " + method);
		return true;
	}
	
}