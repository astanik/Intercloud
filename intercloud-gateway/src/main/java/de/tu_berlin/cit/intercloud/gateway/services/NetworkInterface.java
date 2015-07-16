package de.tu_berlin.cit.intercloud.gateway.services;

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

	public NetworkInterface(LinkType linkRepresentation) {
		super(linkRepresentation);
		// TODO Auto-generated constructor stub
	}

}
