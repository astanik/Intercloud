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

package de.tu_berlin.cit.intercloud.occi.platform;

import java.net.URI;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;



/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 * @author Daniel Thilo Schroeder <daniel.schroeder@mailbox.tu-berlin.de>
 */
@Kind(schema = PlatformSchemas.ApplicationSchema, term = ApplicationKind.ApplicationTerm)
public class ApplicationKind extends Category{

	public final static String ApplicationTitle = "Application Resource";
	
	public final static String ApplicationTerm = "application";
	
	public ApplicationKind(){
		super(ApplicationTitle);
	}
	
	public ApplicationKind(String title){
		super(title);
	}
	
	/**
	 * Name of the appliccation
	 */
	@Attribute(name = "occi.app.name",
			type = AttributeType.STRING,
			mutable = true,
			required = true,
			description = "Name of the appliccation")
	public String name = null;

	/**
	 * URL for contextualizing the app.
	 */
	@Attribute(name = "occi.app.context",
			type = AttributeType.URI,
			mutable = false,
			required = true,
			description = "URL for contextualizing the app.")
	public URI context = null;

	/**
	 * DNS entry.
	 */
	@Attribute(name = "occi.app.url",
			type = AttributeType.URI,
			mutable = false,
			required = true,
			description = "DNS entry.")
	public URI url = null;

	public enum State {
		active, 
		inactive, 
		error
	}
	
	/**
	 * Current state of the application
	 */
	@Attribute(name = "occi.app.state",
			type = AttributeType.ENUM,
			mutable = false,
			required = true,
			description = "Current state of the application")
	public State state = null;


	/**
	 * Human-readable explanation of the current application state
	 */
	@Attribute(name = "occi.app.state.message",
			type = AttributeType.STRING,
			mutable = false,
			required = false,
			description = "Human-readable explanation of the current application state")
	public String message = null;
	
	
	
}
