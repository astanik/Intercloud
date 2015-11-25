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

package de.tu_berlin.cit.intercloud.xmpp.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class allows to persist representations in relation to its path.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Entity
public class RepresentationPersistence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	String path;

	String representation;

	public int get_Id() {
		return id;
	}

	public void set_Id(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRepresentation() {
		return representation;
	}

	public void setRepresentation(String representation) {
		this.representation = representation;
	}

	// @MapKeyColumn
	// @ElementCollection(fetch=FetchType.EAGER)
	// Map<String, String> representations = new HashMap<String, String>();
	//
	// public Map<String, String> getRepresentations() {
	// return this.representations;
	// }
	//
	// public void setRepresentations(Map<String, String> map) {
	// this.representations = map;
	// }

}