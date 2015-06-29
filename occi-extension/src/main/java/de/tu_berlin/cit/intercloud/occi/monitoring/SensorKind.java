package de.tu_berlin.cit.intercloud.occi.monitoring;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeDocument.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryDocument.Category;

public class SensorKind extends Kind {

	public final static String SensorTitle = "Sensor Resource";
	
	public final static String SensorSchema = "http://schema.cit.tu-berlin.de/occi/monitoring#";
	
	public final static String SensorTerm = "sensor";
	
	public SensorKind() {
		super(SensorSchema, SensorTerm, SensorTitle);
	}

	@Override
	protected void defineAttributes(Category category) {
		// create attribute list
		Attribute attribute = category.addNewAttributes().addNewAttribute();
		// define base name
		String baseName = "occi.sensor.";
		
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
