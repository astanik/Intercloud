package de.tu_berlin.cit.intercloud.gateway.openstack;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jclouds.Constants;
import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.keystone.v2_0.config.CredentialTypes;
import org.jclouds.openstack.keystone.v2_0.config.KeystoneProperties;
import org.jclouds.openstack.neutron.v2.NeutronApi;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.FloatingIP;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.domain.KeyPair;
import org.jclouds.openstack.nova.v2_0.domain.RebootType;
import org.jclouds.openstack.nova.v2_0.domain.SecurityGroup;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.domain.Volume;
import org.jclouds.openstack.nova.v2_0.extensions.FloatingIPApi;
import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApi;
import org.jclouds.openstack.nova.v2_0.extensions.SecurityGroupApi;
import org.jclouds.openstack.nova.v2_0.extensions.VolumeApi;
import org.jclouds.openstack.nova.v2_0.extensions.VolumeAttachmentApi;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;
import org.jclouds.openstack.nova.v2_0.options.CreateVolumeOptions;
import org.jclouds.openstack.v2_0.domain.Resource;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.Module;

import de.tu_berlin.cit.intercloud.util.configuration.OpenStackConfig;

public class OpenStackApi implements Closeable {

	private final NovaApi novaApi;
	private final Set<String> regions;

	public static void main(String[] args) throws IOException {
		OpenStackApi jcloudsNova = new OpenStackApi();

		try {
			jcloudsNova.listServers();
			jcloudsNova.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jcloudsNova.close();
		}
	}

	public OpenStackApi() {
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

		// System.out.printf(">> initializing %s%n", novaApi..getApiMetadata());

		regions = novaApi.getConfiguredRegions();
	}

	public void listServers() {
		for (String region : regions) {
			ServerApi serverApi = novaApi.getServerApi(region);

			System.out.println("Servers in " + region);

			for (Server server : serverApi.listInDetail().concat()) {
				System.out.println("  " + server);
			}
		}
	}

	public void listFlavors() {
		for (String region : regions) {
			FlavorApi flavorApi = novaApi.getFlavorApi(region);

			System.out.println("Flavors in " + region);

			for (Flavor server : flavorApi.listInDetail().concat()) {
				System.out.println("  " + server);
			}
		}
	}

	public void listImages() {
		for (String region : regions) {
			ImageApi api = novaApi.getImageApi(region);

			System.out.println("Images in " + region);

			for (Image server : api.listInDetail().concat()) {
				System.out.println("  " + server);
			}
		}
	}

	public void listKeyPairs() {
		for (String region : regions) {
			Optional<KeyPairApi> apiSet = novaApi.getKeyPairApi(region);

			if (!apiSet.isPresent())
				return;

			KeyPairApi api = apiSet.get();

			System.out.println("KeyPairs in " + region);

			FluentIterable<KeyPair> keyPairList = api.list();

			for (int i = 0; i < keyPairList.size(); i++) {
				KeyPair pair = keyPairList.get(i);
				System.out.println("  " + pair);
			}
		}
	}
/*
	public String launch_instance(String name, String image, String flavor,
			String keypair, String network, Iterable<String> secGroup,
			String userData) {
		ServerApi serverApi = novaApi.getServerApi(regions.iterator().next());
		CreateServerOptions options = CreateServerOptions.Builder
				.keyPairName(keypair).networks(network)
				.securityGroupNames(secGroup).userData(userData.getBytes());
		ServerCreated ser = serverApi.create(name, getImageId(image),
				this.getFlavorId(flavor), options);
		return ser.getId();
	}

	public void startVM() {
        for (String region : regions) {
    	novaApi.getServerAdminApi(region).
        }    	
    }
*/
	@Override
	public void close() throws IOException {
		// novaApi.close();
		Closeables.close(novaApi, true);
	}
/*
	public static void main2(String[] args) throws IOException {
		OpenstackClient client;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String username = OpenstackClient.loadPropertyFromEnv(args, reader,
				"OS_USERNAME", null);
		String password = OpenstackClient.loadPropertyFromEnv(args, reader,
				"OS_PASSWORD", null);
		String authUrl = OpenstackClient.loadPropertyFromEnv(args, reader,
				"OS_AUTH_URL", null);
		String tenantName = OpenstackClient.loadPropertyFromEnv(args, reader,
				"OS_TENANT_NAME", null); /*
										 *  * String user =
										 * "masco.kaliyamoorthy@enovance.com";
										 * String password = *
										 * "Masvandayar@003"; String tenant = *
										 * "e2614ec6-8a63-11e3-b8f7-525400872571"
										 * ; String url = *
										 * "https://identity.fr1.cloudwatt.com/v2.0/"
										 * ;
										 */
/*		String keyName = "jckey";
		client = new OpenstackClient(username, password, tenantName, authUrl);
		client.createKeyPair(keyName, "/home/masco/masco_id_rsa.pub");
		client.createSecurityGroup("mySecGroup", "this is discription");
		String nw = client.createNetwork("jcnetwork");
		client.createSubnet(nw, "192.168.199.0/24");
		client.createRouter("jcloudRouter");
		HashSet<String> secGroup = new HashSet<String>();
		secGroup.add(client.getSecurityGroupId("default"));
		String s = client.launch_instance("jcinstance", "cirrosbrowse",
				"s1.cw.small-1", keyName, nw, secGroup, "Simple User Data");
		String volume = client.createVolume(1, "jcVolume");
		String floatingIP = client.getOrCreateFloatingIP(); /*
															 *  * sleep for some
															 * time so the
															 * instance will be
															 * ready so we can
															 * attach * the ip
															 * and volume.
															 */
/*		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.attachIP(floatingIP, s);
		client.attachVolume(volume, s);
		client.rebootServer(s, RebootType.SOFT);
		client.deleteServer(s);
	}
*/
}

class OpenstackClient {
	private final NovaApi novaApi;
	private final NeutronApi neutronApi;
	private final Set<String> zones;
	private String defaultZone = null;

	public OpenstackClient(String user, String password, String tenant,
			String url) {
		Iterable<Module> modules = ImmutableSet
				.<Module> of(new SLF4JLoggingModule());
		Properties overrides = new Properties();
		overrides.setProperty(KeystoneProperties.CREDENTIAL_TYPE,
				CredentialTypes.PASSWORD_CREDENTIALS);
		overrides.setProperty(Constants.PROPERTY_RELAX_HOSTNAME, "true");
		overrides.setProperty(Constants.PROPERTY_TRUST_ALL_CERTS, "true");
		novaApi = ContextBuilder.newBuilder("openstack-nova").endpoint(url)
				.credentials(tenant + ":" + user, password).modules(modules)
				.overrides(overrides).buildApi(NovaApi.class);
		neutronApi = ContextBuilder.newBuilder("openstack-neutron")
				.endpoint(url).credentials(tenant + ":" + user, password)
				.modules(modules).overrides(overrides)
				.buildApi(NeutronApi.class);
		zones = novaApi.getConfiguredZones();
		if (null == defaultZone) {
			defaultZone = zones.iterator().next();
		}
	}

	public static String loadPropertyFromEnv(String args[],
			BufferedReader reader, String propertyName, String defaultValue)
			throws IOException {
		String property = System.getenv(propertyName);
		if (property == null)
			property = defaultValue;
		for (String s : args) {
			if (s.startsWith(propertyName + "=")) {
				property = s.substring(propertyName.length() + 1);
			}
		}
		while (property == null) {
			System.err.print("\nPlease provide " + propertyName + ": ");
			System.err.flush();
			property = reader.readLine();
		}
		return property;
	}

	public void setZone(String zone) {
		if (null != zone && "" == zone) {
			defaultZone = zone;
		}
	}

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
/*
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

	public String launch_instance(String name, String image, String flavor,
			String keypair, String network, Iterable<String> secGroup,
			String userData) {
		ServerApi serverApi = this.novaApi.getServerApiForZone(defaultZone);
		CreateServerOptions options = CreateServerOptions.Builder
				.keyPairName(keypair).networks(network)
				.securityGroupNames(secGroup).userData(userData.getBytes());
		ServerCreated ser = serverApi.create(name, this.getImageId(image),
				this.getFlavorId(flavor), options);
		return ser.getId();
	}

	public void rebootServer(String server, RebootType type) {
		ServerApi serverApi = this.novaApi.getServerApiForZone(defaultZone);
		serverApi.reboot(this.getServerId(server), type);
	}

	public void deleteServer(String server) {
		ServerApi serverApi = this.novaApi.getServerApiForZone(defaultZone);
		serverApi.delete(this.getServerId(server));
	}

	public String getServerId(String server) {
		ServerApi serverApi = this.novaApi.getServerApiForZone(defaultZone);
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
		ImageApi imageApi = this.novaApi.getImageApiForZone(this.defaultZone);
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
		FlavorApi flavorApi = this.novaApi
				.getFlavorApiForZone(this.defaultZone);
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

	public String getVolumeId(String volume) {
		VolumeApi volumeApi = this.novaApi.getVolumeExtensionForZone(
				this.defaultZone).get();
		try {
			Volume volumeObj = volumeApi.get(volume);
			return volumeObj.getId();
		} catch (NullPointerException e) {
			Iterator<? extends Volume> volumeList = volumeApi.list().iterator();
			while (volumeList.hasNext()) {
				Volume v = volumeList.next();
				if (v.getName().equalsIgnoreCase(volume))
					return v.getId();
			}
		}
		throw new NullPointerException("Volume not Found");
	}

	public String getSecurityGroupId(String sg) {
		SecurityGroupApi securityGroupApi = novaApi
				.getSecurityGroupExtensionForZone(defaultZone).get();
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
}
