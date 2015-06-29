package de.tu_berlin.cit.intercloud.occi.sla;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeDocument.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryDocument.Category;

public class OfferKind extends Kind {

	public final static String OfferTitle = "Offer Resource";
	
	public final static String OfferSchema = "http://schema.cit.tu-berlin.de/occi/sla#";
	
	public final static String OfferTerm = "offer";
	
	public OfferKind() {
		super(OfferSchema, OfferTerm, OfferTitle);
	}

	@Override
	protected void defineAttributes(Category category) {
		// create attribute list
		Attribute attribute = category.addNewAttributes().addNewAttribute();
		// define base name
		String baseName = "occi.offer.";
		
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
