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

package de.tu_berlin.cit.intercloud.occi.http.services;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


public class ComputeService {

	private final String representation;
	private final int id;
	private final OCCIServices parent;
	
	public ComputeService(OCCIServices parent, int id, String rep) {
		this.representation = rep;
		this.id = id;
		this.parent = parent;
	}
	
    @GET
    @Produces( MediaType.TEXT_PLAIN )
    String getRepresentation() {
    	return this.representation;
    }

    @DELETE
    void createVM() {
    	this.parent.removeCompute(this.id);
    }

}