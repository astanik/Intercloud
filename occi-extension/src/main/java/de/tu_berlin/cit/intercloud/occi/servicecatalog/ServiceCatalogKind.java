package de.tu_berlin.cit.intercloud.occi.servicecatalog;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeDocument.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryDocument.Category;

public class ServiceCatalogKind extends Kind {

	public final static String CatalogTitle = "Service Catalog Resource";
	
	public final static String CatalogSchema = "http://schema.cit.tu-berlin.de/occi/catalog#";
	
	public final static String CatalogTerm = "service";
	
	public ServiceCatalogKind() {
		super(CatalogSchema, CatalogTerm, CatalogTitle);
	}

	@Override
	protected void defineAttributes(Category category) {
		// create attribute list
		todo
		Attribute attribute = category.addNewAttributes().addNewAttribute();
		// define base name
		String baseName = "occi.compute.";
		
		// define architecture
		attribute.setName(baseName + "architecture");
		attribute.setType("Enum{x86,x64}");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("CPU Architecture of the instance");

		// define cores
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "cores");
		attribute.setType("Integer");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("Number of virtual CPU cores assigned to the instance");
		
		// define host name
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "hostname");
		attribute.setType("String");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("Fully qualified DNS host name for the instance");
		
		// define share
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "share");
		attribute.setType("Integer");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("Relative number of CPU shares for the instance");
		
		// define memory
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "memory");
		attribute.setType("Double");
		attribute.setMutable(true);
		attribute.setRequired(false);
		attribute.setDescription("Minimum RAM in gigabytes allocated to the instance");
		
		// define state
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "state");
		attribute.setType("Enum{active, inactive, suspended, error}");
		attribute.setMutable(false);
		attribute.setRequired(true);
		attribute.setDescription("Current state of the instance");
		
		// define message
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "message");
		attribute.setType("String");
		attribute.setMutable(false);
		attribute.setRequired(false);
		attribute.setDescription("Human-readable explanation of the current instance state");
	}

	@Override
	protected void defineLinks(Category category) {
		// TODO Auto-generated method stub
	}

}
