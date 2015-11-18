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

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.Module;

import de.tu_berlin.cit.intercloud.gateway.openstack.OpenStackComputeTemplates;
import de.tu_berlin.cit.intercloud.gateway.openstack.OpenStackComputeMixin;
import de.tu_berlin.cit.intercloud.gateway.openstack.OpenStackImageMixin;
import de.tu_berlin.cit.intercloud.occi.core.Collection;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.occi.infrastructure.IpNetworkInterfaceMixin;
import de.tu_berlin.cit.intercloud.occi.infrastructure.NetworkInterfaceLink;
import de.tu_berlin.cit.intercloud.occi.infrastructure.StorageLink;
import de.tu_berlin.cit.intercloud.util.configuration.OpenStackConfig;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;

/**
 * Open Stack implementation for compute instances.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Path("/compute")
@Summary("This resource allows for manage compute instances, e.g. creating virtual machines.")
@Classification(kind = ComputeKind.class,
				mixins = {OpenStackComputeMixin.class, OpenStackImageMixin.class, IpNetworkInterfaceMixin.class},
				links = {StorageLink.class, NetworkInterfaceLink.class})
public class Compute extends Collection  implements Closeable {

	private final static Logger logger = LoggerFactory.getLogger(Compute.class);
	private final NovaApi novaApi;
	private final Set<String> regions;
	private String defaultZone = null;

	public Compute() {
		super();
		logger.info("Initializing compute collection ...");
		OpenStackConfig openStackConf = OpenStackConfig.getInstance();
		Iterable<Module> modules = ImmutableSet
				.<Module> of(new SLF4JLoggingModule());

		String provider = "openstack-nova";
		String identity = openStackConf.getTenantName() + ":"
				+ openStackConf.getUserName();
		String credential = openStackConf.getPassword();

		novaApi = ContextBuilder.newBuilder(provider)
				.endpoint(openStackConf.getEndpoint())
				.credentials(identity, credential).modules(modules)
				.buildApi(NovaApi.class);

		regions = novaApi.getConfiguredRegions();
		
		if (defaultZone == null) {
			defaultZone = regions.iterator().next();
		}

		checkExistingInstances();

		logger.info("Initialized compute collection successfully");
	}
	
	private void checkExistingInstances() {
		for (String region : regions) {
			ServerApi serverApi = novaApi.getServerApi(region);
			FlavorApi flavorApi = novaApi.getFlavorApi(region);
			ImageApi imageApi = novaApi.getImageApi(region);
			logger.info("Checking for servers in " + region);
			for (Server server : serverApi.listInDetail().concat()) {
				logger.info("Found server: " + server.getName());
				
				// create a vm resource
				ComputeInstance vm = new ComputeInstance(serverApi, flavorApi, imageApi, server);
				String path = this.addResource(vm);
				logger.info("New VM resource is created at: " + path);
			}
		}
	}

	@XmppMethod(XmppMethod.POST)
    @Consumes(value = OcciXml.MEDIA_TYPE, serializer = OpenStackComputeTemplates.class)
    @Produces(value = UriText.MEDIA_TYPE, serializer = UriText.class)
	public UriText createVM(OpenStackComputeTemplates rep) {
		logger.info("Starting VM instantiation ...");
		// get kind and mixins
		ComputeKind kind = rep.getComputeKind();
		OpenStackComputeMixin computeMixin = rep.getComputeMixin();
		OpenStackImageMixin imageMixin = rep.getImageMixin();
		
		// map IDs according OpenStackComputeTemplates
		String flavorID = kind.getTitle();
		String imageID = imageMixin.getTitle();
		String keypair = computeMixin.keyname;
		String name = computeMixin.name;

		// select zone
		ServerApi serverApi = novaApi.getServerApi(defaultZone);
		FlavorApi flavorApi = novaApi.getFlavorApi(defaultZone);
		ImageApi imageApi = novaApi.getImageApi(defaultZone);

		// set config
		CreateServerOptions options = CreateServerOptions.Builder.keyPairName(keypair);
		// launch VM
		ServerCreated ser = serverApi.create(name, imageID,	flavorID, options);
		Server server = serverApi.get(ser.getId());

		// create a vm resource
		ComputeInstance vm = new ComputeInstance(serverApi, flavorApi, imageApi, server);
		String path = this.addResource(vm);
		logger.info("New VM resource is created at: " + path);
		
		try {
			UriText uri = new UriText(path);
			return uri;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new UriText(); 
		}
	}
	
	private String launchVM(String name, String image, String flavor,
			String keypair, String network, Iterable<String> secGroup,
			String userData) {
		ServerApi serverApi = this.novaApi.getServerApi(defaultZone);
		CreateServerOptions options = CreateServerOptions.Builder
				.keyPairName(keypair).networks(network)
		.securityGroupNames(secGroup).userData(userData.getBytes());
		ServerCreated ser = serverApi.create(name, image,
				flavor, options);
		return ser.getId();
	}
	
	@Override
	public void close() throws IOException {
		Closeables.close(novaApi, true);
	}

	@Override
	protected void finalize( ) throws Throwable   
	{
		this.close();
	   super.finalize( );  
	 }
}