/**
 * Copyright (C) 2012-2015 TU Berlin. All rights reserved.
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

package de.tu_berlin.cit.intercloud.occi.core.classification;

public class Attribute {

	final private String name;
	
	final private String type;
	
	final private Boolean mutable;
	
	final private Boolean required;
	
	final private String pattern;
	
	final private String def;
	
	final private String descriptiopn;

	public Attribute(String name, String type, Boolean mutable, Boolean required) {
		this(name, type, mutable, required, null, null, null);
	}
	
	public Attribute(String name, String type, Boolean mutable, Boolean required, 
			String pattern, String def, String description) {
		this.name = name;
		this.type = type;
		this.mutable = mutable;
		this.required = required;
		this.pattern = pattern;
		this.def = def;
		this.descriptiopn = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public Boolean isMutable() {
		return this.mutable;
	}
	
	public Boolean isRequired() {
		return this.required;
	}
	
	public String getPattern() {
		return this.pattern;
	}
	
	public String getDefault() {
		return this.def;
	}
	
	public String getDescription() {
		return this.descriptiopn;
	}

}
