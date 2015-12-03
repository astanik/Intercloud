package de.tu_berlin.cit.intercloud.occi.core.incarnation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.GDuration;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.LinkCategory;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Mixin;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.ListType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.MapItem;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.MapType;

/**
 * This class converts xmlbeans generated XML documents to OCCI classification
 * objects (kind, mixin, link) and vice versa.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class RepresentationBuilder {

	/**
	 * OCCI object ---> XML document
	 * 
	 * This method converts kinds or mixins to a new CategoryDocument.
	 * 
	 * @param categoryInstance Kind or mixin object.
	 * @return Returns a new Category document
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static CategoryDocument buildRepresentation(Category categoryInstance)
			throws IllegalArgumentException, IllegalAccessException {
		CategoryDocument doc = CategoryDocument.Factory.newInstance();
		CategoryType categoryXml = null;
		if (categoryInstance.getClass().isAnnotationPresent(Kind.class)) {
			categoryXml = doc.addNewCategory().addNewKind();
		} else if (categoryInstance.getClass().isAnnotationPresent(Mixin.class)) {
			categoryXml = doc.addNewCategory().addNewMixin();
		} else {
			throw new RuntimeException(
					"the class instance is not either a kind nor a mixin");
		}

		setClassification(categoryXml, categoryInstance);
		setAttributes(categoryXml, categoryInstance);

		return doc;
	}

	/**
	 * OCCI object ---> XML document
	 * 
	 * This method converts a mixin to XML mixin and appends this mixin to 
	 * an existing DategoryDocument.
	 * 
	 * @param doc An existing instance of a CategoryDocument
	 * @param categoryInstance The mixin to convert
	 * @return The CategoryDocument with the appended mixin
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static CategoryDocument appendMixin(CategoryDocument doc,
			Category categoryInstance) throws IllegalArgumentException,
			IllegalAccessException {
		CategoryType categoryXml = null;
		if (categoryInstance.getClass().isAnnotationPresent(Mixin.class)) {
			categoryXml = doc.addNewCategory().addNewMixin();
		} else {
			throw new RuntimeException("the class instance is not a mixin");
		}

		setClassification(categoryXml, categoryInstance);
		setAttributes(categoryXml, categoryInstance);

		return doc;
	}

	/**
	 * OCCI object ---> XML document
	 * 
	 * This method converts a link to a new LinkType.
	 * 
	 * @param link The link to convert.
	 * @return A new LinkType 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static LinkType buildLinkRepresentation(LinkCategory link) throws IllegalArgumentException, IllegalAccessException {
		LinkType rep;
		if (link.getClass().isAnnotationPresent(Link.class)) {
			rep = LinkType.Factory.newInstance();
			rep.setTarget(link.getTarget());
		} else {
			throw new RuntimeException(
					"the class instance is not a link");
		}

		setClassification(rep, link);
		setAttributes(rep, link);
		return rep;
	}

	/**
	 * OCCI object ---> XML document
	 * 
	 * This method converts a mixin to XML mixin and appends this mixin to 
	 * an existing LinkType.
	 * 
	 * @param rep The existing LinkType document.
	 * @param mixin The mixin to convert 
	 * @return The same LinkType document with the appended mixin
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static LinkType appendLinkMixin(LinkType rep, Category mixin) throws IllegalArgumentException, IllegalAccessException {
		CategoryType categoryXml = null;
		if (mixin.getClass().isAnnotationPresent(Mixin.class)) {
			categoryXml = rep.addNewMixin();
		} else {
			throw new RuntimeException("the class instance is not a mixin");
		}

		setClassification(categoryXml, mixin);
		setAttributes(categoryXml, mixin);

		return rep;
	}

	/**
	 * OCCI object ---> XML document
	 * 
	 * @param categoryXml
	 * @param categoryInstance
	 */
	private static void setClassification(CategoryType categoryXml,
			Category categoryInstance) {
		categoryXml.setSchema(categoryInstance.getSchema());
		categoryXml.setTerm(categoryInstance.getTerm());
		categoryXml.setTitle(categoryInstance.getTitle());
	}

	/**
	 * OCCI object ---> XML document
	 * 
	 * @param categoryXml
	 * @param categoryInstance
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void setAttributes(CategoryType categoryXml,
			Category categoryInstance) throws IllegalArgumentException,
			IllegalAccessException {
		for (Field field : categoryInstance.getClass().getFields()) {
			if (field.isAnnotationPresent(Attribute.class)
					&& field.get(categoryInstance) != null) {
				Attribute attribute = field.getAnnotation(Attribute.class);
				AttributeType attXml = categoryXml.addNewAttribute();
				attXml.setName(attribute.name());
				switch (attribute.type()) {
				case STRING:
					String str = (String) field.get(categoryInstance);
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
					if (!field.getType().isArray())
						throw new RuntimeException(
								"Wrong attribute's signature type");

					attXml.setSIGNATURE((byte[]) field.get(categoryInstance));
					break;
				case KEY:
					if (!field.getType().isArray())
						throw new RuntimeException(
								"Wrong attribute's signature type");

					attXml.setKEY((byte[]) field.get(categoryInstance));
					break;
				case DATETIME:
					attXml.setDATETIME((Calendar) field.get(categoryInstance));
					break;
				case DURATION:
					attXml.setDURATION((GDuration) field.get(categoryInstance));
					break;
				case LIST:
					@SuppressWarnings("unchecked")
					List<String> list = (List<String>) field.get(categoryInstance);
					ListType xmlList = attXml.addNewLIST();
					for(String item : list) {
						xmlList.addItem(item);
					}
					break;
				case MAP:
					@SuppressWarnings("unchecked")
					Map<String, String> map = (Map<String, String>) field.get(categoryInstance);
					MapType xmlMap = attXml.addNewMAP();
					for(String key : map.keySet()) {
						MapItem item = xmlMap.addNewItem();
						item.setKey(key);
						item.setStringValue(map.get(key));
					}
					break;
				default:
					throw new RuntimeException("Undefined attribute type");
				}
			}
		}
	}

	/**
	 * XML document ---> OCCI object
	 * 
	 * This method converts a CategoryDocument to a kind or mixin object.
	 * This method ignores links and their mixins, because several links with
	 * the same classification can exist. Therefore, use the buildLinkRepresentation
	 * method in order to convert a particular LinkType document to an OCCI link or mixin.
	 * 
	 * @param doc The XML document to convert.
	 * @param rep A template instance of the target representation
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Category> T buildRepresentation(CategoryDocument doc,
			T rep) throws IllegalArgumentException, IllegalAccessException {
		// check kind
		CategoryType kind = doc.getCategory().getKind();
		if(correspondsClassification(kind, rep))
			return getAttributes(kind, rep);
		
		// ckeck mixins
		CategoryType[] mixins = doc.getCategory().getMixinArray();
		for(CategoryType mixin : mixins) {
			if(correspondsClassification(mixin, rep))
				return getAttributes(mixin, rep);
		}
		
		// return default
		return rep;
	}

	/**
	 * XML document ---> OCCI object
	 * 
	 * This method converts a particular LinkType document to an OCCI link or mixin.
	 * 
	 * @param doc The XML document to convert.
	 * @param rep A template instance of the target representation
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Category> T buildLinkRepresentation(LinkType doc,
			T rep) throws IllegalArgumentException, IllegalAccessException {
		// check link
		if(correspondsClassification(doc, rep)) {
			rep = getAttributes(doc, rep);
			if(rep instanceof LinkCategory) {
				LinkCategory link = (LinkCategory) rep;
				if(doc.isSetTarget()) {
					link.setTarget(doc.getTarget());
				}
			}
			return rep;
		}
		
		// ckeck mixins
		CategoryType[] mixins = doc.getMixinArray();
		for(CategoryType mixin : mixins) {
			if(correspondsClassification(mixin, rep))
				return getAttributes(mixin, rep);
		}
		
		// return default
		return rep;
	}

	/**
	 * XML document ---> OCCI object
	 * 
	 * Check schema and term whether both types corresponds to each other.
	 * 
	 * @param cat The CategoryType document
	 * @param instance The Category instance
	 * @return Return true if schema and term are equal, else false
	 */
	private static boolean correspondsClassification(CategoryType cat, Category instance) {
		String schema = cat.getSchema();
		String term = cat.getTerm();
		// check
		if(schema.equals(instance.getSchema()) && term.equals(instance.getTerm()))
			return true;
		else
			return false;
	}
	
	/**
	 * XML document ---> OCCI object
	 * 
	 * @param cat
	 * @param rep
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static <T extends Category> T getAttributes(CategoryType cat,
			T rep) throws IllegalArgumentException, IllegalAccessException {
		// set title
		if (cat.isSetTitle())
			rep.setTitle(cat.getTitle());

		// set attributes
		AttributeType[] attributes = cat.getAttributeArray();
		for (int i = 0; i < attributes.length; i++) {
			for (Field field : rep.getClass().getFields()) {
				if (field.isAnnotationPresent(Attribute.class)) {
					Attribute attribute = field.getAnnotation(Attribute.class);
					if (attribute.name().equals(attributes[i].getName())) {
						switch (attribute.type()) {
						case STRING:
							if (attributes[i].isSetSTRING()) {
								String str = new String(
										attributes[i].getSTRING());
								field.set(rep, str);
							} else
								throw new RuntimeException(
										"Attribute string type mismatch");
							break;
						case ENUM:
							@SuppressWarnings("rawtypes")
							Class cls = field.getType();
							if (attributes[i].isSetENUM() && cls.isEnum()) {
								String str = new String(attributes[i].getENUM());
								@SuppressWarnings("unchecked")
								Object value = Enum.valueOf(cls, str);
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute enum type mismatch");
							break;
						case INTEGER:
							if (attributes[i].isSetINTEGER()) {
								Integer value = new Integer(
										attributes[i].getINTEGER());
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute integer type mismatch");
							break;
						case FLOAT:
							if (attributes[i].isSetFLOAT()) {
								Float value = new Float(
										attributes[i].getFLOAT());
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute float type mismatch");
							break;
						case DOUBLE:
							if (attributes[i].isSetDOUBLE()) {
								Double value = new Double(
										attributes[i].getDOUBLE());
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute double type mismatch");
							break;
						case BOOLEAN:
							if (attributes[i].isSetBOOLEAN()) {
								Boolean value = new Boolean(
										attributes[i].getBOOLEAN());
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute boolean type mismatch");
							break;
						case URI:
							if (attributes[i].isSetURI()) {
								String value = new String(
										attributes[i].getURI());
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute uri type mismatch");
							break;
						case SIGNATURE:
							if (attributes[i].isSetSIGNATURE()) {
								byte[] value = attributes[i].getSIGNATURE();
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute signature type mismatch");
							break;
						case KEY:
							if (attributes[i].isSetKEY()) {
								byte[] value = attributes[i].getKEY();
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute key type mismatch");
							break;
						case DATETIME:
							if (attributes[i].isSetDATETIME()) {
								Calendar value = attributes[i].getDATETIME();
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute datetime type mismatch");
							break;
						case DURATION:
							if (attributes[i].isSetDURATION()) {
								GDuration value = attributes[i].getDURATION();
								field.set(rep, value);
							} else
								throw new RuntimeException(
										"Attribute duration type mismatch");
							break;
						case LIST:
							if (attributes[i].isSetLIST()) {
								ListType value = attributes[i].getLIST();
								ArrayList<String> list = new ArrayList<String>();
								String[] array = value.getItemArray();
								for(String item : array) {
									list.add(item);
								}
								field.set(rep, list);
							} else
								throw new RuntimeException(
										"Attribute list type mismatch");
							break;
						case MAP:
							if (attributes[i].isSetMAP()) {
								MapType value = attributes[i].getMAP();
								HashMap<String, String> map = new HashMap<String, String>();
								MapItem[] array = value.getItemArray();
								for(MapItem item : array) {
									map.put(item.getKey(), item.getStringValue());
								}
								field.set(rep, map);
							} else
								throw new RuntimeException(
										"Attribute map type mismatch");
							break;
						default:
							throw new RuntimeException(
									"Undefined attribute type");

						}
					}
				}
			}
		}
		return rep;
	}


}
