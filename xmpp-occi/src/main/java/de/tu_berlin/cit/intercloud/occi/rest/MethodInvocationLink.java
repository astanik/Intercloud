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

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.LinkCategory;

/**
 * This link allows to invoke a predefined remote method.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Link(schema = RestSchemas.RestSchema, term = MethodInvocationLink.MethodInvocationTerm,
		relation = Category.CategorySchema)
public class MethodInvocationLink extends LinkCategory {

	public final static String MethodInvocationTitle = "MethodInvocation Link";
	
	public final static String MethodInvocationTerm = "methodinvocation";
	
	public MethodInvocationLink() {
		super(MethodInvocationTitle);
	}

	public MethodInvocationLink(String title) {
		super(title);
	}

	/**
	 * The type of the method
	 */
	@Attribute(name = "rest.method.type",
			type = AttributeType.STRING,
			mutable = false,
			required = true,
			description = "The type of the method")
	public String methodtype = null;
	
	/**
	 * The media type of the request
	 */
	@Attribute(name = "rest.method.request.mediatype",
			type = AttributeType.STRING,
			mutable = false,
			required = false,
			description = "The media type of the request")
	public String requestmediatype = null;
	
	/**
	 * The media type of the response
	 */
	@Attribute(name = "rest.method.response.mediatype",
			type = AttributeType.STRING,
			mutable = false,
			required = false,
			description = "The media type of the response")
	public String responsemediatype = null;
	
}
