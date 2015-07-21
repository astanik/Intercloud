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

import de.tu_berlin.cit.intercloud.occi.core.IntercloudSchemas;


/**
 * Constants definition for the OCCI Infrastructure Module.
 *  
 * -infrastructure Kinds/Links
 * ---Compute
 * ---Storage
 * ---storageLink
 * ---Network
 * -----IpNetworkMixin
 * ---NetworkInterfaceLink
 * -----IpNetworkInterfaceMixin
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 * @author Daniel Thilo Schroeder <daniel.schroeder@mailbox.tu-berlin.de>
 */
public final class InfrastructureSchemas {
	
	public final static String InfrastructureTag = "infrastructure";
	
	public final static String InfrastructureExtension = IntercloudSchemas.OgfSchemaURL + InfrastructureTag;
	
	public final static String InfrastructureSchema = InfrastructureExtension +"#";
	
	//Mixin Predefinitions
	public final static String ComputeMixinSchema = InfrastructureExtension + "/" + ComputeKind.ComputeTerm + "#";
	
	public final static String StorageMixinSchema = InfrastructureExtension + "/" + StorageKind.StorageTerm + "#";
	
	public final static String StorageLinkMixinSchema = InfrastructureExtension + "/" + StorageLink.StorageLinkTerm + "#";
	
	public final static String NetworkMixinSchema = InfrastructureExtension + "/" + NetworkKind.NetworkTerm + "#";
	
	public final static String NetworkInterfaceMixinSchema = InfrastructureExtension + "/" + NetworkInterfaceLink.NetworkInterfaceTerm + "#";

}
