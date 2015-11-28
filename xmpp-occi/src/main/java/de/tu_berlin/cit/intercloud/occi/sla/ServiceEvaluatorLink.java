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

package de.tu_berlin.cit.intercloud.occi.sla;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.LinkCategory;

/**
 * This link points to a service. Furthermore, it not only links to and describes
 * the service, but it also evaluates the target based on the attributes that describes
 * the service within this link.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Link(schema = SlaSchemas.SlaSchema, term = ServiceEvaluatorLink.ServiceEvaluatorTerm,
		relation = Category.CategorySchema)
public class ServiceEvaluatorLink extends LinkCategory {

	public final static String ServiceEvaluatorTitle = "ServiceEvaluator Link";
	
	public final static String ServiceEvaluatorTerm = "ServiceEvaluator";
	
	public ServiceEvaluatorLink() {
		super(ServiceEvaluatorTitle);
	}

	public ServiceEvaluatorLink(String title) {
		super(title);
	}

	/**
	 * The sensor from which the link retrieves its measurements, i.e. the object
	 */
	@Attribute(name = "intercloud.sla.serviceevaluator.sensor",
			type = AttributeType.STRING,
			mutable = true,
			required = true,
			description = "The sensor from which the link retrieves its measurements, i.e. the object")
	public String sensor = null;
	
	public String getSubject() {
		return this.getTarget();
	}
	
	public void setSubject(String target) {
		this.setTarget(target);
	}
	
	public enum State {
		undefined, 
		violated, 
		fulfilled
	}
	
	/**
	 * Current state of the instance
	 */
	@Attribute(name = "intercloud.sla.serviceevaluator.state",
			type = AttributeType.ENUM,
			mutable = false,
			required = true,
			description = "Current state of the instance: Enum{undefined, violated, fulfilled}")
	public State state = null;

	/**
	 * Human-readable explanation of the current instance state
	 */
	@Attribute(name = "intercloud.sla.serviceevaluator.message",
			type = AttributeType.STRING,
			mutable = false,
			required = false,
			description = "Human-readable explanation of the current instance state")
	public String message = null;

}
