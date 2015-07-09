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

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeDocument.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryDocument.Category;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkDocument.Link;

public class ComputeKind extends Kind {

	public final static String ComputeTitle = "Compute Resource";
	
	public final static String ComputeSchema = "http://schema.ogf.org/occi/infrastructure#";
	
	public final static String ComputeTerm = "compute";
	
	public final static String NetworkInterfaceTerm = "networkinterface";
	
	public ComputeKind() {
		super(ComputeSchema, ComputeTerm, ComputeTitle);
	}

	@Override
	protected void defineAttributes(Category category) {
		// create attribute list
		Attribute attribute = category.addNewAttributes().addNewAttribute();
		// define base name
		String baseName = "occi.compute.";
		
		// define architecture
		attribute.setName(baseName + "architecture");
		attribute.setType("Enum{x86,x64}");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("CPU Architecture of the instance");

		// define cores
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "cores");
		attribute.setType("Integer");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("Number of virtual CPU cores assigned to the instance");
		
		// define host name
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "hostname");
		attribute.setType("String");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("Fully qualified DNS host name for the instance");
		
		// define share
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "share");
		attribute.setType("Integer");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("Relative number of CPU shares for the instance");
		
		// define memory
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "memory");
		attribute.setType("Double");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("Minimum RAM in gigabytes allocated to the instance");
		
		// define state
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "state");
		attribute.setType("Enum{active, inactive, suspended, error}");
		attribute.setMutable(false);
		attribute.setRequired(true);
		attribute.setDescription("Current state of the instance");
		
		// define message
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "message");
		attribute.setType("String");
		attribute.setMutable(false);
		attribute.setRequired(false);
		attribute.setDescription("Human-readable explanation of the current instance state");
	}

	@Override
	protected void defineLinks(Category category) {
		// create link list
		Link link = category.addNewLinks().addNewLink();
		// set category
		link.setCategory(ComputeSchema + NetworkInterfaceTerm);
		// define base name
		String baseName = "occi.networkinterface.";
		
		// define mac
		Attribute attribute = link.addNewAttribute();
		attribute.setName(baseName + "mac");
		attribute.setType("String");
		attribute.setMutable(true);
		attribute.setRequired(true);
		attribute.setDescription("MAC address associated with the link's device interface");
		
		// define state
		attribute = link.addNewAttribute();
		attribute.setName(baseName + "state");
		attribute.setType("Enum{active, inactive, error}");
		attribute.setMutable(false);
		attribute.setRequired(true);
		attribute.setDescription("Current state of the instance");
		
		// define message
		attribute = link.addNewAttribute();
		attribute.setName(baseName + "message");
		attribute.setType("String");
		attribute.setMutable(false);
		attribute.setRequired(false);
		attribute.setDescription("Human-readable explanation of the current instance state");
	}
	
}
