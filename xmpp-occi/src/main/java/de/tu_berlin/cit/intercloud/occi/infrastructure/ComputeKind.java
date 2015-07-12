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
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;


@Kind(schema = ComputeKind.ComputeSchema, term = ComputeKind.ComputeTerm)
public class ComputeKind extends Category {

	public final static String ComputeTitle = "Compute Resource";
	
	public final static String ComputeSchema = "http://schema.ogf.org/occi/infrastructure#";
	
	public final static String ComputeTerm = "compute";
	
	public final static String NetworkInterfaceTerm = "networkinterface";
	
	public ComputeKind() {
		super(ComputeTitle);
	}

	public ComputeKind(String title) {
		super(title);
	}

	public enum Architecture {
		x86,
		x64
	}
	
	@Attribute(name = "occi.compute.architecture",
			type = AttributeType.ENUM,
			mutable = true,
			description = "CPU Architecture of the instance: Enum{x86,x64}")
	public Architecture architecture = null;

	@Attribute(name = "occi.compute.cores",
			type = AttributeType.INTEGER,
			mutable = true,
			value = "1",
			description = "Number of virtual CPU cores assigned to the instance")
	public Integer cores = 1;

	@Attribute(name = "occi.compute.hostname",
			type = AttributeType.STRING,
			mutable = true,
			description = "Fully qualified DNS host name for the instance")
	public String hostname = null;

	@Attribute(name = "occi.compute.share",
			type = AttributeType.INTEGER,
			mutable = true,
			description = "Relative number of CPU shares for the instance")
	public Integer share = null;

	@Attribute(name = "occi.compute.memory",
			type = AttributeType.DOUBLE,
			mutable = true,
			description = "Minimum RAM in gigabytes allocated to the instance")
	public Double memory = null;

	public enum State {
		active, 
		inactive, 
		suspended, 
		error
	}
	
	@Attribute(name = "occi.compute.state",
			type = AttributeType.ENUM,
			required = true,
			description = "Current state of the instance: Enum{active, inactive, suspended, error}")
	public State state = null;

	@Attribute(name = "occi.compute.message",
			type = AttributeType.STRING,
			description = "Human-readable explanation of the current instance state")
	public String message = null;

}
