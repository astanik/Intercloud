package de.tu_berlin.cit.intercloud.gateway.services;

import org.jclouds.openstack.nova.v2_0.domain.Address;

import de.tu_berlin.cit.intercloud.occi.core.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.infrastructure.IpNetworkInterfaceMixin;
import de.tu_berlin.cit.intercloud.occi.infrastructure.NetworkInterfaceLink;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Classification(mixins = {IpNetworkInterfaceMixin.class},
				links = {NetworkInterfaceLink.class})
public class NetworkInterface extends Link {

	private final Address address;
	
	public NetworkInterface(Address add) {
		super();
		this.address = add;
	}

}
