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

package de.tu_berlin.cit.intercloud.gateway.services;

import org.jclouds.openstack.neutron.v2.features.NetworkApi;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.infrastructure.IpNetworkingMixin;
import de.tu_berlin.cit.intercloud.occi.infrastructure.NetworkKind;
import de.tu_berlin.cit.rwx4j.annotations.PathID;

/**
 * Open Stack implementation for network instances.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Summary("This resource allows for manage a particular netwok.")
@Classification(kind = NetworkKind.class,
				mixins = {IpNetworkingMixin.class})
public class NetworkInstance extends Resource{

	private final NetworkApi networkApi;
	
	private final org.jclouds.openstack.neutron.v2.domain.Network network;

/*
	public NetworkInstance(OcciXml occiRepresentation) {
		super(occiRepresentation);
	}
*/
	
	public NetworkInstance(NetworkApi networkApi,
			org.jclouds.openstack.neutron.v2.domain.Network network) {
		this.networkApi = networkApi;
		this.network = network;
	}
	

	@Override
	public OcciXml createRepresentation() {
		OcciXml rep = new OcciXml();

		// define network kind
		/*
		NetworkKind kind = new NetworkKind();
		kind.label = this.network.getName();
		this.network.getStatus()
		Flavor flavor = flavorApi.get(server.getFlavor().getId());
		kind.cores = flavor.getVcpus();
		kind.memory = (double) flavor.getRam();
		if (this.network.getStatus().equals(Server.Status.ACTIVE)) {
			kind.state = ComputeKind.State.active;
		} else if (this.server.getStatus().equals(Server.Status.SUSPENDED)
				|| this.server.getStatus().equals(Server.Status.REBOOT)) {
			kind.state = ComputeKind.State.suspended;
		} else if (this.server.getStatus().equals(Server.Status.PAUSED)) {
			kind.state = ComputeKind.State.inactive;
		} else {
			kind.state = ComputeKind.State.error;
		}

		// define compute mixin
		OpenStackComputeMixin computeMixin = new OpenStackComputeMixin();
		computeMixin.name = server.getName();
		computeMixin.keyname = server.getKeyName();

		// define image mixin
		OpenStackImageMixin imageMixin = new OpenStackImageMixin();
		Image image = imageApi.get(server.getImage().getId());
		imageMixin.name = image.getName();
		imageMixin.mindisk = image.getMinDisk();
		imageMixin.minram = image.getMinRam();

		try {
			// create document
			CategoryDocument doc;
			doc = RepresentationBuilder.buildRepresentation(kind);
			doc = RepresentationBuilder.appendMixin(doc, computeMixin);
			doc = RepresentationBuilder.appendMixin(doc, imageMixin);
			// create representation
			rep = new OcciXml(doc);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		return rep;
	}


}
