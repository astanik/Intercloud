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

package de.tu_berlin.cit.intercloud.occi.core.annotations;

import de.tu_berlin.cit.intercloud.occi.core.IntercloudSchemas;


/**
 * Basis class for all kinds, links, and mixins.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class Category {

	public final static String CategorySchema = IntercloudSchemas.OgfSchemaURL + "core#";

	public final static String CategoryTerm = "category";

	private String title;

	public Category() {
		this("OCCI Core Model");
	}

	public Category(String title) {
		this.title = title;
	}

	public String getSchema() {
		String schema = CategorySchema;
		if (this.getClass().isAnnotationPresent(Kind.class)) {
			schema = this.getClass().getAnnotation(Kind.class).schema();
		} else if (this.getClass().isAnnotationPresent(Mixin.class)) {
			schema = this.getClass().getAnnotation(Mixin.class).schema();
		} else if (this.getClass().isAnnotationPresent(Link.class)) {
			schema = this.getClass().getAnnotation(Link.class).schema();
		}
		return schema;
	}

	public String getTerm() {
		String term = "";
		if (this.getClass().isAnnotationPresent(Kind.class)) {
			term = this.getClass().getAnnotation(Kind.class).term();
		} else if (this.getClass().isAnnotationPresent(Mixin.class)) {
			term = this.getClass().getAnnotation(Mixin.class).term();
		} else if (this.getClass().isAnnotationPresent(Link.class)) {
			term = this.getClass().getAnnotation(Link.class).term();
		}
		return term;
	}

	public String getTitle() {
		if (this.title != null)
			return this.title;
		else
			return "";
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String toTextPlain() {
		StringBuilder text = new StringBuilder();
		text.append("Category: " + this.getTerm() + "; \n");
		text.append("scheme=" + this.getSchema() + "; \n");
		if (this.getClass().isAnnotationPresent(Kind.class)) {
			text.append("class=" + "kind; \n");
		} else if (this.getClass().isAnnotationPresent(Mixin.class)) {
			text.append("class=" + "mixin; \n");
		} else if (this.getClass().isAnnotationPresent(Link.class)) {
			text.append("class=" + "link; \n");
		}
		text.append("title=" + this.getTitle() + "; \n");
		// Attribute[] attr =
		// this.doc.getCategory().getAttributes().getAttributeArray();
		// text.append("attributes=");
		// for (int i = 0; i < attr.length; i++) {
		//	text.append(" " + attr[i].getName());
		// }
		// text.append("; \n");
		// Link[] link = this.doc.getCategory().getLinks().getLinkArray();
		// text.append("links=");
		// for (int i = 0; i < link.length; i++) {
		//	text.append(" " + link[i].getCategory());
		// }
		// text.append("; \n");
		return text.toString();
	}

	@Override
	public String toString() {
		return this.toTextPlain();
	}

}