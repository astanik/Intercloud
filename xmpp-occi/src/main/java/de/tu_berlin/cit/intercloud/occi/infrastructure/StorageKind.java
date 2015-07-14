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

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Kind(schema = InfrastructureSchemas.StorageSchema, term = StorageKind.StorageTerm)
public class StorageKind extends Category {

	public final static String StorageTitle = "Storage Resource";
	
	public final static String StorageTerm = "storage";
	
	public StorageKind() {
		super(StorageTitle);
	}

	public StorageKind(String title) {
		super(title);
	}

	/**
	 * Storage size in gigabytes of the instance.
	 */
	@Attribute(name = "occi.storage.size",
			type = AttributeType.FLOAT,
			mutable = true,
			required = true,
			description = "Storage size in gigabytes of the instance.")
	public Float size = null;
	
	public enum State {
		online,
		offline,
		error
	}
	
	/**
	 * Current status of the instance.
	 */
	@Attribute(name = "occi.storage.state",
			type = AttributeType.ENUM,
			mutable = false,
			required = true,
			description = "Current status of the instance.")
	public State state = null;
	
	/**
	 * Human-readable explanation of the current instance state.
	 */
	@Attribute(name = "occi.storage.state.message",
			type = AttributeType.STRING,
			mutable = false,
			required = false,
			description = "Human-readable explanation of the current instance state.")
	public String message = null;

}
