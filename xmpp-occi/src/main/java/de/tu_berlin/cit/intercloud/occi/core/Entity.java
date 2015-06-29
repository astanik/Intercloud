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

public abstract class Entity {
	
	final private String id;
	
	private String title;
	
	final private Kind kind;
	
	final private List<Mixin> mixins;
	
	protected Entity(String id, Kind kind) {
		this(id, "", kind);
	}

	protected Entity(String id, String title, Kind kind) {
		this(id, title, kind, new ArrayList<Mixin>());
	}
	
	protected Entity(String id, String title, Kind kind, List<Mixin> mixins) {
		this.id = id;
		this.title = title;
		this.kind = kind;
		this.mixins = mixins;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Kind getKind() {
		return this.kind;
	}
	
	public List<Mixin> getMixins() {
		return this.mixins;
	}
	
}