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
import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.occi.core.Entity;

public abstract class Kind extends Category {

	final private Class<? extends Kind> parent;
	
	final private List<Action> actions;
	
	final private List<Entity> entities;
	
	protected Kind(URI schema, String term, String title, Class<? extends Kind> parent) {
		super(schema, term, title);
		this.parent = parent;
		this.actions = new ArrayList<Action>();
		this.entities = new ArrayList<Entity>();
	}

	protected Kind(URI schema, String term, String title) {
		this(schema, term, title, null);
	}
	
	public Class<? extends Kind> getParent() {
		return this.parent;
	}
	
	public List<Action> getAction() {
		return this.actions;
	}
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
	@Override
	public String toText() {
    	StringBuilder text = new StringBuilder();
    	text.append(this.getCategoryText("kind"));

    	if(this.parent != null) {
			try {
	    		Kind instance;
				instance = this.parent.newInstance();
	    		text.append("parent=" + instance.getSchema() + "#" + instance.getTerm() + "; \n");
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
