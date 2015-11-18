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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jclouds.openstack.nova.v2_0.domain.Address;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;

import com.google.common.collect.Multimap;

import de.tu_berlin.cit.intercloud.gateway.openstack.OpenStackComputeMixin;
import de.tu_berlin.cit.intercloud.gateway.openstack.OpenStackImageMixin;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.RepresentationBuilder;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.occi.infrastructure.IpNetworkInterfaceMixin;
import de.tu_berlin.cit.intercloud.occi.infrastructure.NetworkInterfaceLink;
import de.tu_berlin.cit.intercloud.occi.infrastructure.StorageLink;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Parameter;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Result;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppAction;

/**
 * Open Stack implementation for compute instances.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Summary("This resource allows for manage a particular virtual machine.")
@Classification(kind = ComputeKind.class, mixins = {
		OpenStackComputeMixin.class, OpenStackImageMixin.class,
		IpNetworkInterfaceMixin.class }, links = { StorageLink.class,
		NetworkInterfaceLink.class })
public class ComputeInstance extends Resource {

	// private static UUID TemplateID =
	// UUID.fromString("f509099b-0da9-4f96-8fe6-7b20f6614381");

	private final ServerApi serverApi;

	private final FlavorApi flavorApi;

	private final ImageApi imageApi;

	private final Server server;

	/*
	 * public ComputeInstance(OcciXml rep) { super(rep); }
	 */

	public ComputeInstance(ServerApi serverApi, FlavorApi flavorApi,
			ImageApi imageApi, Server server) {
		super();
		this.serverApi = serverApi;
		this.flavorApi = flavorApi;
		this.imageApi = imageApi;
		this.server = server;
		Map<String, Collection<Address>> networks = this.server.getAddresses().asMap();
		Set<String> netIDs = networks.keySet();
		for(String net : netIDs) {
			System.out.println("Networks: " + net);
			Collection<Address> addresses = networks.get(net);
			for(Address add : addresses) {
				System.out.println("Addresses: " + add.toString());
			}
		}
	}

	@Override
	public OcciXml createRepresentation() {
		OcciXml rep = new OcciXml();

		// define compute kind
		ComputeKind kind = new ComputeKind();
		Flavor flavor = flavorApi.get(server.getFlavor().getId());
		kind.cores = flavor.getVcpus();
		kind.memory = (double) flavor.getRam();
		if (this.server.getStatus().equals(Server.Status.ACTIVE)) {
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

		return rep;
	}

	// @Action("start")
	// @Result()
	public Boolean start(String message) {
		// starting vm
		System.out.println("Stating vm with message: " + message);
		return true;
	}

	@XmppAction(value = "stop", documentation = "Stop this virtual machine")
	@Result(documentation = "Returns true if the vm has been stopped successfully")
	public Boolean stop(
			@Parameter(value = "method", documentation = "The method used for stopping this vm") String method) {
		// stop the vm after "delay" seconds
		System.out.println("Stopping vm with method: " + method);
		return true;
	}

}