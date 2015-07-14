package de.tu_berlin.cit.intercloud.occi.infrastructure;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class InfrastructureSchemas {
	
	
	private final static String SchemaURL = "http://schema.ogf.org/occi/";
	
	private final static String InfrastructureTag = "infrastructure";
	
	private final static String InfrastructureSchema = SchemaURL + InfrastructureTag +"#";
	
	/**
	 * 
	 * -infrastructure Kinds/Links
	 * ---Compute
	 * ---Storage
	 * ---storageLink
	 * ---Network
	 * -----IpNetworkMixin
	 * ---NetworkInterfaceLink
	 * -----IpNetworkInterfaceMixin
	 */
	
	//Kinds/Links
	public final static String ComputeSchema = InfrastructureSchema;
	
	public final static String StorageSchema = InfrastructureSchema;
	
	public final static String StorageLinkSchema = InfrastructureSchema;
	
	public final static String NetworkSchema = InfrastructureSchema;
	
	public final static String NetworkInterfaceSchema = InfrastructureSchema;
	
	
	//Mixins
	public final static String NetworkInterfaceMixinSchema = SchemaURL + InfrastructureTag + NetworkInterfaceLink.NetworkInterfaceTerm + "#";
	
	public final static String NetworkMixinSchema = SchemaURL + InfrastructureTag + NetworkKind.NetworkTerm + "#";
}
