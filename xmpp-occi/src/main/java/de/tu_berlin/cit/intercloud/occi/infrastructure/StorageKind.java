package de.tu_berlin.cit.intercloud.occi.infrastructure;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeDocument.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryDocument.Category;

public class StorageKind extends Kind {

	public final static String StorageTitle = "Storage Resource";
	
	public final static String StorageSchema = "http://schema.ogf.org/occi/infrastructure#";
	
	public final static String StorageTerm = "storage";
	
	public StorageKind() {
		super(StorageSchema, StorageTerm, StorageTitle);
	}

	@Override
	protected void defineAttributes(Category category) {
		// create attribute list
		Attribute attribute = category.addNewAttributes().addNewAttribute();
		// define base name
		String baseName = "occi.storage.";
		
		// define state
//		attribute = category.getAttributes().addNewAttribute();
//		attribute.setName(baseName + "state");
//		attribute.setType("Enum{active, inactive, suspended, error}");
//		attribute.setMutable(false);
//		attribute.setRequired(true);
//		attribute.setDescription("Current state of the instance");
	}

	@Override
	protected void defineLinks(Category category) {
		// TODO Auto-generated method stub
	}

}
