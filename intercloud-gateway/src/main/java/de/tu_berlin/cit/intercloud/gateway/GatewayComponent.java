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

package de.tu_berlin.cit.intercloud.gateway;

import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.occi.infrastructure.InfrastructureSchemas;
import de.tu_berlin.cit.intercloud.occi.monitoring.SensorKind;
import de.tu_berlin.cit.intercloud.occi.sla.ManagerKind;
import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.component.ResourceContainerComponent;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public class GatewayComponent extends ResourceContainerComponent {
	
	public GatewayComponent(ResourceContainer container) {
		super(container);
	}

	@Override
	public String getName() {
		return ServiceNames.GatewayComponentName;
	}

	@Override
	public String getDescription() {
		return "This is one Intercloud Gateway service.";
	}
	
	@Override
	protected String[] discoInfoFeatureNamespaces() {
		return (new String[] { "urn:xmpp:rest:xwadl", "urn:xmpp:rest:xml",
				InfrastructureSchemas.ComputeSchema + ComputeKind.ComputeTerm,
				ManagerKind.ManagerSchema + ManagerKind.ManagerTerm,
				SensorKind.SensorSchema + SensorKind.SensorTerm});
	}

	@Override
	protected void rootDiscovered() {
		super.rootDiscovered();
		
		// publish resources
		
	}

	@Override
	protected void handleRestXWADL(ResourceTypeDocument parse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleRestXML(ResourceDocument parse) {
		// TODO Auto-generated method stub
		
	}

}
