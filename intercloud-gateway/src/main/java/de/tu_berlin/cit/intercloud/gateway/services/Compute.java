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
import java.util.Iterator;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.domain.KeyPair;
import org.jclouds.openstack.nova.v2_0.domain.SecurityGroup;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApi;
import org.jclouds.openstack.nova.v2_0.extensions.SecurityGroupApi;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;
import org.jclouds.openstack.v2_0.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
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

	@XmppMethod(value = XmppMethod.POST, documentation = "This method creates a new virtual machine.")
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

/*
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
*/
	public void listServers() {
		for (String region : regions) {
			ServerApi serverApi = novaApi.getServerApi(region);

			logger.info("Servers in " + region);

			for (Server server : serverApi.listInDetail().concat()) {
				logger.info("  " + server);
			}
		}
	}

	public void listFlavors() {
		for (String region : regions) {
			FlavorApi flavorApi = novaApi.getFlavorApi(region);

			logger.info("Flavors in " + region);

			for (Flavor server : flavorApi.listInDetail().concat()) {
				logger.info("  " + server);
			}
		}
	}

	public void listImages() {
		for (String region : regions) {
			ImageApi api = novaApi.getImageApi(region);

			logger.info("Images in " + region);

			for (Image server : api.listInDetail().concat()) {
				logger.info("  " + server);
			}
		}
	}

	public void listKeyPairs() {
		for (String region : regions) {
			Optional<KeyPairApi> apiSet = novaApi.getKeyPairApi(region);

			if (!apiSet.isPresent())
				return;

			KeyPairApi api = apiSet.get();

			logger.info("KeyPairs in " + region);

			FluentIterable<KeyPair> keyPairList = api.list();

			for (int i = 0; i < keyPairList.size(); i++) {
				KeyPair pair = keyPairList.get(i);
				logger.info("  " + pair);
			}
		}
	}


	public String getServerId(String server) {
		ServerApi serverApi = this.novaApi.getServerApi(defaultZone);
		try {
			Server serverObj = serverApi.get(server);
			return serverObj.getId();
		} catch (NullPointerException e) {
			for (Resource s : serverApi.list().concat()) {
				if (s.getName().equalsIgnoreCase(server))
					return s.getId();
			}
		}
		throw new NullPointerException("Server not found");
	}

	public String getImageId(String image) {
		ImageApi imageApi = this.novaApi.getImageApi(defaultZone);
		try {
			Image imageObj = imageApi.get(image);
			return imageObj.getId();
		} catch (NullPointerException e) {
			for (Resource i : imageApi.list().concat()) {
				if (i.getName().equalsIgnoreCase(image))
					return i.getId();
			}
		}
		throw new NullPointerException("Image not found");
	}

	public String getFlavorId(String flavor) {
		FlavorApi flavorApi = this.novaApi.getFlavorApi(defaultZone);
		try {
			Flavor flavorObj = flavorApi.get(flavor);
			return flavorObj.getId();
		} catch (NullPointerException e) {
			for (Resource f : flavorApi.list().concat()) {
				if (f.getName().equalsIgnoreCase(flavor))
					return f.getId();
			}
		}
		throw new NullPointerException("Flavor not found");
	}

	public String getSecurityGroupId(String sg) {
		SecurityGroupApi securityGroupApi = novaApi.getSecurityGroupApi(defaultZone).get();
		try {
			SecurityGroup securityGroup = securityGroupApi.get(sg);
			return securityGroup.getId();
		} catch (Exception e) {
			Iterator<? extends SecurityGroup> sgList = securityGroupApi.list()
					.iterator();
			while (sgList.hasNext()) {
				SecurityGroup group = sgList.next();
				if (group.getName().equalsIgnoreCase(sg))
					return group.getId();
			}
		}
		throw new NullPointerException("Security Group not found");
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
	
/**	main >>
 * 		client.createKeyPair(keyName, "/home/masco/masco_id_rsa.pub");
		client.createSecurityGroup("mySecGroup", "this is discription");
		String nw = client.createNetwork("jcnetwork");
		client.createSubnet(nw, "192.168.199.0/24");
		client.createRouter("jcloudRouter");
		HashSet<String> secGroup = new HashSet<String>();
		secGroup.add(client.getSecurityGroupId("default"));
		String s = client.launch_instance("jcinstance", "cirrosbrowse",
				"s1.cw.small-1", keyName, nw, secGroup, "Simple User Data");
		String volume = client.createVolume(1, "jcVolume");
		String floatingIP = client.getOrCreateFloatingIP(); 
		// sleep for some time so the instance will be
		// ready so we can attach the ip and volume.
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.attachIP(floatingIP, s);
		client.attachVolume(volume, s);
		client.rebootServer(s, RebootType.SOFT);
		client.deleteServer(s);

 * 
 * 
	public void createKeyPair(String name, String path) throws IOException {
		KeyPairApi keypairApi = this.novaApi.getKeyPairExtensionForZone(
				this.defaultZone).get();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			line = sb.toString();
			keypairApi.createWithPublicKey(name, line);
		} catch (IOException e) {
			System.out.println("ERROR::Given file path is not valid.");
		} finally {
			br.close();
		}
	}
	public String createSecurityGroup(String name, String description) {
		SecurityGroupApi securityGroupApi = novaApi
				.getSecurityGroupExtensionForZone(defaultZone).get();
		SecurityGroup securityGroup = securityGroupApi.createWithDescription(
				name, description);
		return securityGroup.getId();
	}

	public String getOrCreateFloatingIP() {
		List<FloatingIP> freeIP = new LinkedList<FloatingIP>();
		FloatingIPApi floatingIPApi = this.novaApi
				.getFloatingIPExtensionForZone(this.defaultZone).get();
		Iterator<? extends FloatingIP> floatingIP = floatingIPApi.list()
				.iterator();
		while (floatingIP.hasNext()) {
			FloatingIP ip = floatingIP.next();
			if (ip.getInstanceId() == null) {
				freeIP.add(ip);
			}
		}
		if (freeIP.size() > 0) {
			return freeIP.get(0).getIp();
		} else {
			return floatingIPApi.create().getIp();
		}
	}

	public void attachIP(String ip, String server) {
		FloatingIPApi floatingIPApi = this.novaApi
				.getFloatingIPExtensionForZone(this.defaultZone).get();
		floatingIPApi.addToServer(ip, this.getServerId(server));
	}

	public String createVolume(int size, String name) {
		VolumeApi volumeApi = this.novaApi.getVolumeExtensionForZone(
				this.defaultZone).get();
		Volume vol = volumeApi
				.create(1, CreateVolumeOptions.Builder.name(name));
		return vol.getId();
	}

	public void attachVolume(String volume, String server, String device) {
		if (device == null)
			device = "";
		VolumeAttachmentApi volumeAttachmentApi = this.novaApi
				.getVolumeAttachmentExtensionForZone(this.defaultZone).get();
		volumeAttachmentApi.attachVolumeToServerAsDevice(
				this.getVolumeId(volume), this.getServerId(server), device);
	}

	public void attachVolume(String volume, String server) {
		this.attachVolume(volume, server, "");
	}
*/
	
}