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

package de.tu_berlin.cit.intercloud.occi.infrastructure;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;

@Kind(schema = NetworkKind.NetworkSchema, term = NetworkKind.NetworkTerm)
public class NetworkKind extends Category {

	public final static String NetworkTitle = "Network Resource";
	
	public final static String NetworkSchema = "http://schema.ogf.org/occi/infrastructure#";
	
	public final static String NetworkTerm = "network";
	
	public NetworkKind() {
		super(NetworkTitle);
	}

	public NetworkKind(String title) {
		super(title);
	}


}
