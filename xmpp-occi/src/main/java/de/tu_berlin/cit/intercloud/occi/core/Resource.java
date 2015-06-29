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

import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.classification.Mixin;

public class Resource extends Entity {

	private List<Link> links;
	
	private String summary;
	
	public Resource(String id, String title, Kind kind, List<Mixin> mixins) {
		this(id, title, kind, mixins, new ArrayList<Link>(), "");
	}
	
	public Resource(String id, String title, Kind kind, List<Mixin> mixins, List<Link> links, String summary) {
		super(id, title, kind, mixins);
		this.links = links;
		this.summary = summary;
	}
	
	public List<Link> getLinks() {
		return this.links;
	}
	
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public String getSummary() {
		return this.summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
