package de.tu_berlin.cit.intercloud.gateway.services;

import org.jclouds.openstack.nova.v2_0.domain.Address;

import de.tu_berlin.cit.intercloud.occi.core.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.RepresentationBuilder;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.infrastructure.IpNetworkInterfaceMixin;
import de.tu_berlin.cit.intercloud.occi.infrastructure.NetworkInterfaceLink;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Classification(mixins = { IpNetworkInterfaceMixin.class }, links = { NetworkInterfaceLink.class })
public class NetworkInterface extends Link {

	private final Address address;

	public NetworkInterface(String target, Address add) {
		super(target);
		this.address = add;
		this.setLinkTypeRepresentation(this.getLinkTypeRepresentation());
	}

	public NetworkInterface(Address add) {
		super();
		this.address = add;
	}

	@Override
	public LinkType getLinkTypeRepresentation() {
		// define link
		NetworkInterfaceLink link = new NetworkInterfaceLink();
		link.state = NetworkInterfaceLink.State.active;
		String target = this.getParent().getParent().getParent().getPath();
		target = target + "/Network";
		logger.info("Set network interface target: " + target);
		link.setTarget(this.getTarget());

		// define mixin
		IpNetworkInterfaceMixin mixin = new IpNetworkInterfaceMixin();
		mixin.allocation = IpNetworkInterfaceMixin.Allocation.Static;
		mixin.address = this.address.getAddr();

		// create link document
		LinkType rep;
		try {
			rep = RepresentationBuilder.buildLinkRepresentation(link);
			rep = RepresentationBuilder.appendLinkMixin(rep, mixin);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rep = LinkType.Factory.newInstance();
		}

		// update link representation
		this.setLinkTypeRepresentation(rep);
		return rep;
	}

}
