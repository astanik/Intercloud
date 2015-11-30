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

package de.tu_berlin.cit.intercloud.sla.mixins;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Mixin;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;
import de.tu_berlin.cit.intercloud.occi.sla.SlaSchemas;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceEvaluatorLink;

/**
 * Availability Mixin associated with the guarantee term and connected to the 
 * Availability Event stream from the Complex Event Processor.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Mixin(schema = SlaSchemas.ServiceEvaluatorMixinSchema, term = AvailabilityMixin.AvailabilityMixinTerm,
		applies = SlaSchemas.SlaSchema + ServiceEvaluatorLink.ServiceEvaluatorTerm)
public class AvailabilityMixin extends Category {

	public final static String AvailabilityMixinTitle = "Availability Mixin";
	
	public final static String AvailabilityMixinTerm = "availability";
	
	public AvailabilityMixin() {
		super(AvailabilityMixinTitle);
	}

	public AvailabilityMixin(String title) {
		super(title);
	}
	
	/**
	 * The service level objective.
	 */
	@Attribute(name = "intercloud.sla.availability.slo",
			type = AttributeType.DOUBLE,
			mutable = true,
			required = true,
			description = "The service level objective.")
	public Double slo = null;

}