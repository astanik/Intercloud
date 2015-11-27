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

package de.tu_berlin.cit.intercloud.occi.rest;

import java.util.HashMap;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.LinkCategory;

/**
 * This link allows to invoke a predefined remote action.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Link(schema = RestSchemas.RestSchema, term = ActionInvocationLink.ActionInvocationTerm,
		relation = Category.CategorySchema)
public class ActionInvocationLink extends LinkCategory {

	public final static String ActionInvocationTitle = "ActionInvocation Link";
	
	public final static String ActionInvocationTerm = "actioninvocation";
	
	public ActionInvocationLink() {
		super(ActionInvocationTitle);
	}

	public ActionInvocationLink(String title) {
		super(title);
	}

	/**
	 * The name of the action
	 */
	@Attribute(name = "rest.action.type",
			type = AttributeType.STRING,
			mutable = false,
			required = true,
			description = "The name of the action")
	public String actionname = null;

	/**
	 * The parameter list of the action
	 */
	@Attribute(name = "rest.action.parameters",
			type = AttributeType.MAP,
			mutable = false,
			required = false,
			description = "The parameter list of the action while key is the name and value the type")
	public HashMap<String, String> parameters = null;
	
	/**
	 * The result type of the action
	 */
	@Attribute(name = "rest.action.result",
			type = AttributeType.STRING,
			mutable = false,
			required = false,
			description = "The result type of the action")
	public String resulttype = null;

}
