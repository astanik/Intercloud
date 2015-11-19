/**
 * Copyright 2010-2015 Complex and Distributed IT Systems, TU Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tu_berlin.cit.intercloud.occi.core.incarnation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Mixin;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeClassificationDocument.AttributeClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.CategoryClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.LinkClassification;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.MixinClassification;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public final class ClassificationRegistry {

	private final static Logger logger = LoggerFactory
			.getLogger(ClassificationRegistry.class);

	private static ClassificationRegistry singleton = new ClassificationRegistry();

	private final Map<String, Class<? extends Category>> classMapping;

	
	/**
	 * A private Constructor prevents any other class from instantiating.
	 */
	private ClassificationRegistry() {
		logger.info("Retrieving namespace constants ...");
		this.classMapping = NamespaceConstants.mapping;
	}

	/**
	 * Static 'instance' method
	 */
	public static ClassificationRegistry getInstance() {
		return singleton;
	}

	// public static <T extends Category> T newInstance(OcciXml xml, ) {

	// }

	/**
	 * Method to build a list of supported Kind, Mixins, and Links.
	 * 
	 * @param instance The instance to analyze
	 * @return
	 */
	public static List<String> buildTypeList(ResourceInstance instance) {
		logger.info("Start building " + instance.getClass().getSimpleName()
				+ " type list ...");
		ArrayList<String> list = new ArrayList<String>();
		list = buildTypeList(instance.getClass(), list);

		logger.info("Type list has been build: ");
		logger.info(list.toString());
		return list;
	}

	/**
	 * Recursive method to build a list of supported Kind, Mixins, and Links.
	 * This method selects the classification annotation of the sub class and
	 * allows thus to override classification annotations of super classes.
	 * 
	 * @param resourceClass
	 * @param list
	 * @return
	 */
	private static ArrayList<String> buildTypeList(Class<? extends Object> resourceClass, ArrayList<String> list) {
		if (resourceClass.isAnnotationPresent(Classification.class)) {
			Classification classification = resourceClass.getAnnotation(
					Classification.class);
			// add kind
			ClassificationType kind = getKindClassificationType(classification);
			if (kind != null) {
				list.add(kind.getCategory());
			}
			// add mixins
			List<ClassificationType> mixins = getMixinClassificationTypes(classification);
			for(int i=0; i<mixins.size(); i++) {
				list.add(mixins.get(i).getCategory());
			}
			// add links
			List<ClassificationType> links = getLinkClassificationTypes(classification);
			for(int i=0; i<links.size(); i++) {
				list.add(links.get(i).getCategory());
			}
		} else {
			Class<? extends Object> superClass = resourceClass.getSuperclass();
			if (superClass != null) {
				list = buildTypeList(superClass, list);
			}
		}
		return list;
	}

	/**
	 * Method to build a classification document 
	 * of supported Kind, Mixins, and Links according to
	 * the OCCI classification schema.
	 * 
	 * @param instance The instance to analyze
	 * @return
	 */
	public static ClassificationDocument buildClassification(ResourceInstance instance) {
		logger.info("Start building " + instance.getClass().getSimpleName()
				+ " classification ...");
		ClassificationDocument doc = ClassificationDocument.Factory
				.newInstance();

		doc.addNewClassification();
		doc = buildClassification(instance.getClass(), doc);

		logger.info("Classification document has been build: ");
		logger.info(doc.toString());
		return doc;
	}

	/**
	 * Recursive method to build a classification document 
	 * of supported Kind, Mixins, and Links according to
	 * the OCCI classification schema.
	 * This method selects the classification annotation of the sub class and
	 * allows thus to override classification annotations of super classes.
	 * 
	 * @param resourceClass
	 * @param doc
	 * @return
	 */
	private static ClassificationDocument buildClassification(Class<? extends Object> resourceClass,
			ClassificationDocument doc) {
		if (resourceClass.isAnnotationPresent(Classification.class)) {
			Classification classification = resourceClass.getAnnotation(
					Classification.class);
			appendKindClassification(doc, classification);
			appendMixinClassification(doc, classification);
			appendLinkClassification(doc, classification);
		} else {
			Class<? extends Object> superClass = resourceClass.getSuperclass();
			if (superClass != null) {
				doc = buildClassification(superClass, doc);
			}
		}
		return doc;
	}

	private static List<ClassificationType> getLinkClassificationTypes(Classification classification) {
		Class<? extends Category>[] links = classification.links();
		ArrayList<ClassificationType> list = new ArrayList<ClassificationType>();
		for (int i = 0; i < links.length; i++) {

			if (!links[i].isAnnotationPresent(Link.class)) {
				throw new RuntimeException("the class is not a link kind");
			}

			list.add(new ClassificationType(links[i].getAnnotation(Link.class).schema(), links[i].getAnnotation(Link.class).term()));
		}
		return list;
	}

	private static void appendLinkClassification(ClassificationDocument doc,
			Classification classification) {
		Class<? extends Category>[] links = classification.links();

		// append links
		for (int i = 0; i < links.length; i++) {

			if (!links[i].isAnnotationPresent(Link.class)) {
				throw new RuntimeException("the class is not a link");
			}

			// create link element
			LinkClassification docLink = doc.getClassification()
					.addNewLinkType();

			docLink.setSchema(links[i].getAnnotation(Link.class).schema());
			docLink.setTerm(links[i].getAnnotation(Link.class).term());
			docLink.setRelation(links[i].getAnnotation(Link.class).relation());
			// TODO: add optional title

			appendAttributeClassification(docLink, links[i]);
		}
	}

	private static List<ClassificationType> getMixinClassificationTypes(Classification classification) {
		Class<? extends Category>[] mixins = classification.mixins();
		ArrayList<ClassificationType> list = new ArrayList<ClassificationType>();
		for (int i = 0; i < mixins.length; i++) {

			if (!mixins[i].isAnnotationPresent(Mixin.class)) {
				throw new RuntimeException("the class is not a mixin");
			}

			list.add(new ClassificationType(mixins[i].getAnnotation(Mixin.class).schema(), mixins[i].getAnnotation(Mixin.class).term()));
		}
		return list;
	}

	private static void appendMixinClassification(ClassificationDocument doc,
			Classification classification) {
		Class<? extends Category>[] mixins = classification.mixins();

		for (int i = 0; i < mixins.length; i++) {

			if (!mixins[i].isAnnotationPresent(Mixin.class)) {
				throw new RuntimeException("the class is not a mixin");
			}

			// create mixin element
			MixinClassification docMixin = doc.getClassification()
					.addNewMixinType();

			docMixin.setSchema(mixins[i].getAnnotation(Mixin.class).schema());
			docMixin.setTerm(mixins[i].getAnnotation(Mixin.class).term());
			docMixin.setAppliesArray(mixins[i].getAnnotation(Mixin.class)
					.applies());
			// TODO: add optional title

			appendAttributeClassification(docMixin, mixins[i]);
		}
	}

	private static ClassificationType getKindClassificationType(Classification classification) {
		Class<? extends Category> kind = classification.kind();
		if (kind.equals(Category.class)) {
			// no kind is set
			// this is not an exception!
			return null;
		}

		if (!kind.isAnnotationPresent(Kind.class)) {
			// the class is not a kind
			throw new RuntimeException("the class is not a kind");
		}

		return new ClassificationType(kind.getAnnotation(Kind.class).schema(),
				kind.getAnnotation(Kind.class).term());
	}

	private static void appendKindClassification(ClassificationDocument doc,
			Classification classification) {
		Class<? extends Category> kind = classification.kind();

		ClassificationType type = getKindClassificationType(classification);
		if (type == null) {
			// no kind is set
			// this is not an exception!
			return;
		}

		// create kind element
		CategoryClassification docKind = doc.getClassification()
				.addNewKindType();

		docKind.setSchema(type.getSchema());
		docKind.setTerm(type.getTerm());
		// TODO: add optional title

		appendAttributeClassification(docKind, kind);
	}

	private static void appendAttributeClassification(CategoryClassification docKind,
			Class<? extends Category> kind) {

		for (Field field : kind.getFields()) {
			if (field.isAnnotationPresent(Attribute.class)) {
				Attribute attribute = field.getAnnotation(Attribute.class);
				AttributeClassification attXml = docKind
						.addNewAttributeClassification();
				attXml.setName(attribute.name());
				attXml.setType(AttributeType.Enum.forString(attribute.type()
						.toString()));
				attXml.setMutable(attribute.mutable());
				attXml.setRequired(attribute.required());
				attXml.setDefault(attribute.value());
				attXml.setDescription(attribute.description());
			}
		}
	}

	public Map<String, Class<? extends Category>> getClassMapping() {
		return this.classMapping;
	}

	public void addClassMapping(String key, Class<? extends Category> value) {
		this.classMapping.put(key, value);
	}
}
