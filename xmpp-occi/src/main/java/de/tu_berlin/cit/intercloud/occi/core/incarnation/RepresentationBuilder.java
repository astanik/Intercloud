package de.tu_berlin.cit.intercloud.occi.core.incarnation;

import java.lang.reflect.Field;
import java.util.Calendar;

import org.apache.xmlbeans.GDuration;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Mixin;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;

public class RepresentationBuilder {

	public static CategoryDocument buildRepresentation(Category categoryInstance) throws IllegalArgumentException, IllegalAccessException {
		CategoryDocument doc = CategoryDocument.Factory.newInstance();
		CategoryType categoryXml = null;
		if(categoryInstance.getClass().isAnnotationPresent(Kind.class)) {
			categoryXml = doc.addNewCategory().addNewKind();
		} else if(categoryInstance.getClass().isAnnotationPresent(Mixin.class)) {
			categoryXml = doc.addNewCategory().addNewMixin();
		} else {
			throw new RuntimeException("the class instance is not either a kind nor a mixin");
		}
	
		// set classification
		categoryXml.setSchema(categoryInstance.getSchema());
		categoryXml.setTerm(categoryInstance.getTerm());
		categoryXml.setTitle(categoryInstance.getTitle());
		
		// set attributes
		for (Field field : categoryInstance.getClass().getFields()) {
			if (field.isAnnotationPresent(Attribute.class) && field.get(categoryInstance) != null) {
				Attribute attribute = field.getAnnotation(Attribute.class);
				AttributeType attXml = categoryXml.addNewAttribute();
				attXml.setName(attribute.name());
				switch(attribute.type()) {
				case STRING:
					String str = (String)field.get(categoryInstance);
					attXml.setSTRING(str);
					break;
				case ENUM:
					Object en = field.get(categoryInstance);
					attXml.setENUM(en.toString());
					break;
				case INTEGER:
					Integer i = (Integer) field.get(categoryInstance);
					attXml.setINTEGER(i);
					break;
				case FLOAT:
					Float f = (Float) field.get(categoryInstance);
					attXml.setFLOAT(f);
					break;
				case DOUBLE:
					Double d = (Double) field.get(categoryInstance);
					attXml.setDOUBLE(d);
					break;
				case BOOLEAN:
					Boolean b = (Boolean) field.get(categoryInstance);
					attXml.setBOOLEAN(b);
					break;
				case URI:
					attXml.setURI(field.get(categoryInstance).toString());
					break;
				case SIGNATURE:
					if(!field.getType().isArray())
						throw new RuntimeException("Wrong attribute's signature type");
					
					attXml.setSIGNATURE((byte[])field.get(categoryInstance));
					break;
				case KEY:
					if(!field.getType().isArray())
						throw new RuntimeException("Wrong attribute's signature type");

					attXml.setKEY((byte[])field.get(categoryInstance));
					break;
				case DATETIME:
					attXml.setDATETIME((Calendar) field.get(categoryInstance));
					break;
				case DURATION:
					attXml.setDURATION((GDuration) field.get(categoryInstance));
					break;
				default:
					throw new RuntimeException("Undefined attribute type");
				}
			}
		}
		
		return doc;
	}

}
