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

import java.net.URI;
import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.Entity;

public abstract class Mixin extends Category {

	final private List<Mixin> dependencies;
	
	final private List<Action> actions;

	final private List<Kind> kinds;
	
	private List<Entity> entities;

	protected Mixin(URI schema, String term, String title, List<Attribute> attributes, List<Mixin> dependencies, List<Action> actions, List<Kind> kinds, List<Entity> entities) {
		super(schema, term, title, attributes);
		this.dependencies = dependencies;
		this.actions = actions;
		this.kinds = kinds;
		this.entities = entities;
	}

	public List<Mixin> getDependencies() {
		return this.dependencies;
	}
	
	public List<Action> getActions() {
		return this.actions;
	}
	
	public List<Kind> getKinds() {
		return this.kinds;
	}
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	
	@Override
	public String toText() {
    	StringBuilder text = new StringBuilder();
    	text.append(this.getCategoryText("mixin"));

    	if(!this.dependencies.isEmpty()) {
        	text.append("dependencies=");
        	for(int i = 0; i < this.dependencies.size(); i++) {
        		text.append(" " + this.dependencies.get(i).getSchema() + "#" + this.dependencies.get(i).getTerm());
        	}
        	text.append("; \n");
    		
    	}
    	
    	if(!this.actions.isEmpty()) {
        	text.append("actions=");
        	for(int i = 0; i < this.actions.size(); i++) {
        		text.append(" " + this.actions.get(i).getSchema() + "#" + this.actions.get(i).getTerm());
        	}
        	text.append("; \n");
    	}
    	
    	return text.toString();
	}

}
