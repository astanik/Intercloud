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

package de.tu_berlin.cit.intercloud.gateway.openstack;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Mixin;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.occi.infrastructure.InfrastructureSchemas;


/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Mixin(schema = OpenStackSchemas.OpenStackComputeMixinSchema, term = OpenStackComputeMixin.OpenStackComputeTerm,
		applies = InfrastructureSchemas.InfrastructureSchema + ComputeKind.ComputeTerm)
public class OpenStackComputeMixin extends Category {

	public final static String OpenStackComputeTitle = "Open Stack Compute Mixin";
	
	public final static String OpenStackComputeTerm = "openstackcompute";
	
	public OpenStackComputeMixin() {
		super(OpenStackComputeTitle);
	}

	public OpenStackComputeMixin(String title) {
		super(title);
	}
	
	/**
	 * The name of the virtual machine.
	 */
	@Attribute(name = "openstack.compute.name",
			type = AttributeType.STRING,
			mutable = true,
			required = true,
			description = "The name of the virtual machine.")
	public String name = null;
	
	/**
	 * The name of the key to access the virtual machine.
	 */
	@Attribute(name = "openstack.compute.keyname",
			type = AttributeType.STRING,
			mutable = true,
			required = true,
			description = "The name of the key to access the virtual machine.")
	public String keyname = null;
	
	

}
