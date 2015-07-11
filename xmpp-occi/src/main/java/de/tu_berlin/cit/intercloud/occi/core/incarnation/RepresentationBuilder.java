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
			if (field.isAnnotationPresent(Attribute.class)) {
				Attribute attribute = field.getAnnotation(Attribute.class);
				AttributeType attXml = categoryXml.addNewAttribute();
				attXml.setName(attribute.name());
				switch(attribute.type()) {
				case STRING:
					attXml.setSTRING((String)field.get(categoryInstance));
					break;
				case ENUM:
					attXml.setENUM(field.get(categoryInstance).toString());
					break;
				case INTEGER:
					attXml.setINTEGER(field.getInt(categoryInstance));
					break;
				case FLOAT:
					attXml.setFLOAT(field.getFloat(categoryInstance));
					break;
				case DOUBLE:
					attXml.setDOUBLE(field.getDouble(categoryInstance));
					break;
				case BOOLEAN:
					attXml.setBOOLEAN(field.getBoolean(categoryInstance));
					break;
				case URI:
					attXml.setURI(field.get(categoryInstance).toString());
					break;
				case SIGNATURE:
					attXml.setSIGNATURE((byte[])field.get(categoryInstance));
					break;
				case KEY:
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
