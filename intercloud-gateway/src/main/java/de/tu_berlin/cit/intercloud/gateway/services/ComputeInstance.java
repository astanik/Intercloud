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

import org.jclouds.openstack.nova.v2_0.domain.Address;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.domain.RebootType;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.gateway.openstack.OpenStackComputeMixin;
import de.tu_berlin.cit.intercloud.gateway.openstack.OpenStackImageMixin;
import de.tu_berlin.cit.intercloud.occi.core.Link;
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
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Parameter;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Result;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppAction;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;

/**
 * Open Stack implementation for compute instances.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Summary("This resource allows for managing a particular virtual machine.")
@Classification(kind = ComputeKind.class, mixins = { OpenStackComputeMixin.class, OpenStackImageMixin.class,
		IpNetworkInterfaceMixin.class }, links = { StorageLink.class, NetworkInterfaceLink.class })
public class ComputeInstance extends Resource {

	private final static Logger logger = LoggerFactory.getLogger(ComputeInstance.class);

	private final ServerApi serverApi;

	private final FlavorApi flavorApi;

	private final ImageApi imageApi;

	private final Server server;

	/*
	 * public ComputeInstance(OcciXml rep) { super(rep); }
	 */

	public ComputeInstance(ServerApi serverApi, FlavorApi flavorApi, ImageApi imageApi, Server server) {
		super();
		logger.info("Initializing compute instance " + server.getName() + " ...");
		this.serverApi = serverApi;
		this.flavorApi = flavorApi;
		this.imageApi = imageApi;
		this.server = server;
		
		checkExistingLinks();
		logger.info("Initialized compute instance " + server.getName() + " successfully");
	}

	private void checkExistingLinks() {
		Map<String, Collection<Address>> networks = this.server.getAddresses().asMap();
		Set<String> netIDs = networks.keySet();
		logger.info("Checking for network interfaces of " + server.getName());
		for (String net : netIDs) {
			logger.info("Found network: " + net);
			Collection<Address> addresses = networks.get(net);
			for (Address add : addresses) {
				logger.info("Found network interface: " + add.toString());
				// create a network interface link
				NetworkInterface eth = new NetworkInterface(add);
				String path = this.addResource(eth);
				logger.info("New network interface link is created at: " + path);
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

			// add link representation to resource representation
			for(ResourceInstance potLink : this.getResources()) {
				if(potLink instanceof Link) {
					rep.addLink(((Link) potLink).getLinkTypeRepresentation());
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rep;
	}

	@Override
	@XmppMethod(value = XmppMethod.DELETE, documentation = "Terminate and delete this virtual machine")
	public void deleteResource() {
		logger.info("Deleting VM " + server.getName() + " ...");
		serverApi.delete(server.getId());
		super.deleteResource();
		logger.info("VM " + server.getName() + " has been deleted");
	}

	@XmppAction(value = "start", documentation = "Start this virtual machine")
	@Result(documentation = "Returns true if the vm has been started successfully")
	public Boolean start() {
		// start the vm
		logger.info("Starting VM " + server.getName() + " ...");
		serverApi.start(server.getId());
		logger.info("VM " + server.getName() + " has been started");
		return true;
	}

	@XmppAction(value = "stop", documentation = "Stop this virtual machine")
	@Result(documentation = "Returns true if the vm has been stopped successfully")
	public Boolean stop() {
		// stop the vm
		logger.info("Stopping VM " + server.getName() + " ...");
		serverApi.stop(server.getId());
		logger.info("VM " + server.getName() + " has been stopped");
		return true;
	}

	@XmppAction(value = "reboot", documentation = "Reboot this virtual machine")
	@Result(documentation = "Returns true if the vm has been rebooted successfully")
	public Boolean reboot(
			@Parameter(value = "rebootType", documentation = "The type (HARD or SOFT) used for rebooting this vm") String type) {
		// reboot the vm
		logger.info("Rebooting (" + RebootType.HARD + "," + RebootType.SOFT + 
				") VM " + server.getName() + " ..." + type);
		serverApi.reboot(server.getId(), RebootType.valueOf(type));
		logger.info("VM " + server.getName() + " has been rebooted");
		return true;
	}

}