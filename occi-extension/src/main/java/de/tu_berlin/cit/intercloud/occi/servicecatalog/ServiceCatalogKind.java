package de.tu_berlin.cit.intercloud.occi.servicecatalog;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeDocument.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryDocument.Category;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkDocument.Link;

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
		Attribute attribute = category.addNewAttributes().addNewAttribute();
		// define base name
		String baseName = "occi.catalog.";
		
		// define price
		attribute.setName(baseName + "price");
		attribute.setType("Double");
		attribute.setMutable(true);
		attribute.setRequired(true);
		attribute.setDescription("The price for the described service");

		// define currency
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "currency");
		attribute.setType("String");
		attribute.setMutable(true);
		attribute.setRequired(true);
		attribute.setDescription("The currency of the price");
		
		// define billing increments
		attribute = category.getAttributes().addNewAttribute();
		attribute.setName(baseName + "billingincrements");
		attribute.setType("String");
		attribute.setMutable(true);
		attribute.setRequired(true);
		attribute.setDescription("The billing increments of the price, e.g. per GB, per hour, per second, etc.");
	}

	@Override
	protected void defineLinks(Category category) {
		// create link list
		Link link = category.addNewLinks().addNewLink();

		// set category
		link.setCategory(CatalogSchema + "link");
	}

}
