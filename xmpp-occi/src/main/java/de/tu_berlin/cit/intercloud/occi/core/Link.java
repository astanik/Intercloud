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

package de.tu_berlin.cit.intercloud.occi.core;

import java.net.URI;
import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.classification.Mixin;

public class Link extends Entity {

	private Resource source;
	
	private URI target;
	
	public Link(String id, String title, Kind kind, List<Mixin> mixins, Resource source, URI target) {
		super(id, title, kind, mixins);
		this.source = source;
		this.target = target;
	}
	
	public Resource getSource() {
		return this.source;
	}
	
	public void setSource(Resource source) {
		this.source = source;
	}
	
	public URI getTarget() {
		return this.target;
	}
	
	public void setTarget(URI target) {
		this.target = target;
	}
	
}
