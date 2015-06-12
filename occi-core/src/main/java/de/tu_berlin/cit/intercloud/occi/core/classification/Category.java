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

public abstract class Category {
	
	final static public String CategorySchema = "http://schema.ogf.org/occi/core#";
	
	final private URI schema;
	
	final private String term;
	
	final private String title;
	
	final private List<Attribute> attributes;
	
	protected Category(URI schema, String term, String title, List<Attribute> attributes) {
		this.schema = schema;
		this.term = term;
		this.title = title;
		this.attributes = attributes;
	}
	
	public URI getSchema() {
		return this.schema;
	}
	
	public String getTerm() {
		return this.term;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public List<Attribute> getAttributes() {
		return this.attributes;
	}
	
	abstract public String toText();

	protected String getCategoryText(String className) {
    	StringBuilder text = new StringBuilder();
    	text.append("Category: " + this.getTerm() + "; \n");
    	text.append("scheme=" + this.getSchema().getPath() + "#; \n");
    	text.append("class=" + className + "; \n");
    	text.append("title=" + this.getTitle() + "; \n");
    	text.append("attributes=");
    	for(int i = 0; i < this.attributes.size(); i++) {
    		text.append(" " + this.attributes.get(i).getName());
    	}
    	text.append("; \n");
    	return text.toString();
	}

	@Override
	public String toString() {
		return this.toText();
	}
	
}