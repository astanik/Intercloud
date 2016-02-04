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

import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.neutron.v2.NeutronApi;
import org.jclouds.openstack.neutron.v2.features.NetworkApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

import de.tu_berlin.cit.intercloud.configuration.OpenStackConfig;
import de.tu_berlin.cit.intercloud.occi.core.Collection;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.infrastructure.IpNetworkingMixin;
import de.tu_berlin.cit.intercloud.occi.infrastructure.NetworkKind;
import de.tu_berlin.cit.rwx4j.annotations.Path;

/**
 * Open Stack implementation for network instances.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Path("/network")
@Summary("This resource allows for manage network instances.")
@Classification(kind = NetworkKind.class,
				mixins = {IpNetworkingMixin.class})
public class Network extends Collection{
	
	private final static Logger logger = LoggerFactory.getLogger(Network.class);
	private final NeutronApi neutronApi;
	private final Set<String> regions;
	private String defaultZone = null;

	public Network() {
		super();
		logger.info("Initializing network collection ...");
		OpenStackConfig openStackConf = OpenStackConfig.getInstance();
		Iterable<Module> modules = ImmutableSet
				.<Module> of(new SLF4JLoggingModule());

		String provider = "openstack-neutron";
		String identity = openStackConf.getTenantName() + ":"
				+ openStackConf.getUserName();
		String credential = openStackConf.getPassword();

		neutronApi = ContextBuilder.newBuilder(provider)
				.endpoint(openStackConf.getEndpoint())
				.credentials(identity, credential).modules(modules)
				.buildApi(NeutronApi.class);

		regions = neutronApi.getConfiguredRegions();
		
		if (defaultZone == null) {
			defaultZone = regions.iterator().next();
		}

		checkExistingInstances();

		logger.info("Initialized network collection successfully");
	}

	private void checkExistingInstances() {
		for (String region : regions) {
			NetworkApi networkApi = neutronApi.getNetworkApi(region);
//			FlavorApi flavorApi = novaApi.getFlavorApi(region);
//			ImageApi imageApi = novaApi.getImageApi(region);
			logger.info("Checking for networks in " + region);
			for (org.jclouds.openstack.neutron.v2.domain.Network network : networkApi.list().concat()) {
				logger.info("Found network: " + network.getName());
				
				// create a network resource
				NetworkInstance net = new NetworkInstance(networkApi, network);
				String path = this.addResource(net, network.getName());
				logger.info("New network resource is created at: " + path);
			}
		}
	}
	
	/*
	 * String nw = client.createNetwork("jcnetwork");
	 * client.createSubnet(nw, "192.168.199.0/24");
	 * client.createRouter("jcloudRouter");
	 * 
	 * 
		public String createNetwork(String name) {
			NetworkApi networkApi = neutronApi.getNetworkApiForZone(defaultZone);
			CreateNetworkOptions createNetworkOptions = CreateNetworkOptions
					.builder().name(name).build();
			Network network = networkApi.create(createNetworkOptions);
			return network.getId();
		}

		public String createSubnet(String network, String cidr) {
			SubnetApi subnetApi = neutronApi.getSubnetApiForZone(defaultZone);
			Subnet subnet = subnetApi.create(network, 4, cidr);
			return subnet.getId();
		}

		public String createRouter(String name) {
			RouterApi routerApi = neutronApi.getRouterExtensionForZone(defaultZone)
					.get();
			CreateRouterOptions options = CreateRouterOptions.builder().name(name)
					.adminStateUp(true).build();
			Router router = routerApi.create(options);
			return router.getId();
		}
	*/


}
