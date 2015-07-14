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

package de.tu_berlin.cit.intercloud.occi.infrastructure;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.annotations.LinkCategory;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 * @author Daniel Thilo Schroeder <daniel.schroeder@mailbox.tu-berlin.de>
 */
@Link(schema = InfrastructureSchemas.NetworkInterfaceSchema, term = NetworkInterfaceLink.NetworkInterfaceTerm,
		relation = InfrastructureSchemas.NetworkSchema + NetworkKind.NetworkTerm)
public class NetworkInterfaceLink extends LinkCategory {

	public final static String NetworkInterfaceTitle = "NetworkInterface Link";
	
	public final static String NetworkInterfaceTerm = "networkinterface";
	
	public NetworkInterfaceLink() {
		super(NetworkInterfaceTitle);
	}

	public NetworkInterfaceLink(String title) {
		super(title);
	}

	/**
	 * Identifier that relates the link to the link’s device interface
	 */
	@Attribute(name = "occi.networkinterface.interface",
			type = AttributeType.STRING,
			mutable = false,
			required = true,
			description = "Identifier that relates the link to the link’s device interface")
	public String deviceinterface = null;  // interface is a java keyword
	
	/**
	 * MAC address associated with the link's device interface
	 */
	@Attribute(name = "occi.networkinterface.mac",
			type = AttributeType.STRING,
			mutable = true,
			required = true,
			description = "MAC address associated with the link's device interface")
	public String mac = null;

	public enum State {
		active, 
		inactive, 
		error
	}
	
	/**
	 * Current state of the instance
	 */
	@Attribute(name = "occi.networkinterface.state",
			type = AttributeType.ENUM,
			mutable = false,
			required = true,
			description = "Current state of the instance: Enum{active, inactive, error}")
	public State state = null;

	/**
	 * Human-readable explanation of the current instance state
	 */
	@Attribute(name = "occi.networkinterface.message",
			type = AttributeType.STRING,
			mutable = false,
			required = false,
			description = "Human-readable explanation of the current instance state")
	public String message = null;

}
