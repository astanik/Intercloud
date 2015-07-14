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

package de.tu_berlin.cit.intercloud.root.services;

import java.net.URISyntaxException;

import de.tu_berlin.cit.intercloud.occi.core.Collection;
import de.tu_berlin.cit.intercloud.occi.core.OcciListXml;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument.Category;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryListDocument;
import de.tu_berlin.cit.intercloud.occi.servicecatalog.ServiceCatalogMixin;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Summary("This resource allows for manage "
		+ "the overall intercloud service catalog.")
@Classification(mixins = {ServiceCatalogMixin.class})
public abstract class AbstractComputeCatalog extends Collection {

	protected AbstractComputeCatalog() {
		super();
	}

	@XmppMethod(XmppMethod.GET)
    @Consumes(value = OcciXml.MEDIA_TYPE, serializer = OcciXml.class)
	@Produces(value = OcciListXml.MEDIA_TYPE, serializer = OcciListXml.class)
	public OcciListXml searchSlaTemplates(OcciXml requirements) {
		CategoryListDocument catalogList = CategoryListDocument.Factory.newInstance();
		catalogList.addNewCategoryList();
		java.util.Collection<ResourceInstance> resources = this.getResources();
		for(ResourceInstance res : resources) {
			if(res instanceof TemplateInstance) {
				TemplateInstance templateInst = (TemplateInstance) res;
				if(matchRequirements(templateInst.getRepresentation(), requirements)) {
					// add template to list
					Category cat = catalogList.getCategoryList().addNewCategory();
					Category templCat = templateInst.getRepresentation().getDocument().getCategory();
					if(templCat.isSetKind())
						cat.setKind(templCat.getKind());
					if(templCat.getMixinArray().length > 0)
						cat.setMixinArray(templCat.getMixinArray());
				}
			} else if(res instanceof AbstractComputeCatalog) {
				AbstractComputeCatalog catalogInst = (AbstractComputeCatalog) res;
				OcciListXml subList = catalogInst.searchSlaTemplates(requirements);
				// copy all elements
				Category[] categories = subList.getDocument().getCategoryList().getCategoryArray();
				for(int i=0; i < categories.length; i++) {
					// add template to list
					Category cat = catalogList.getCategoryList().addNewCategory();
					if(categories[i].isSetKind())
						cat.setKind(categories[i].getKind());
					if(categories[i].getMixinArray().length > 0)
						cat.setMixinArray(categories[i].getMixinArray());
				}
			}
		}
		return new OcciListXml(catalogList);
	}

	private boolean matchRequirements(OcciXml occiXml, OcciXml requirements) {
		Category requCat = requirements.getDocument().getCategory();
		Category category = occiXml.getDocument().getCategory();
		
		// compare kind attributes
		if(requCat.isSetKind() && category.isSetKind()) {
			AttributeType[] attr = requCat.getKind().getAttributeArray();
			for(int i=0; i<attr.length; i++) {
				if(!containsEqualAttribute(category.getKind().getAttributeArray(), attr[i]))
					return false;
			}
		}
		
		return true;
	}

	private boolean containsEqualAttribute(AttributeType[] attrList,
			AttributeType attribute) {

		for(int k=0; k < attrList.length; k++) {
			if(attribute.getName().equals(attrList[k].getName()))
				if(attribute.isSetBOOLEAN()&& attrList[k].isSetBOOLEAN() &&
					attribute.getBOOLEAN() == attrList[k].getBOOLEAN())
					return true;
				else if(attribute.isSetBOOLEAN()&& attrList[k].isSetBOOLEAN() &&
					attribute.getBOOLEAN() == attrList[k].getBOOLEAN())
					return true;
				else if(attribute.isSetSTRING()&& attrList[k].isSetSTRING() &&
						attribute.getSTRING().equals(attrList[k].getSTRING()))
						return true;
				else if(attribute.isSetENUM()&& attrList[k].isSetENUM() &&
						attribute.getENUM().equals(attrList[k].getENUM()))
						return true;
				else if(attribute.isSetINTEGER()&& attrList[k].isSetINTEGER() &&
						attribute.getINTEGER() == attrList[k].getINTEGER())
						return true;
				else if(attribute.isSetFLOAT()&& attrList[k].isSetFLOAT() &&
						attribute.getFLOAT() == attrList[k].getFLOAT())
						return true;
				else if(attribute.isSetDOUBLE()&& attrList[k].isSetDOUBLE() &&
						attribute.getDOUBLE() == attrList[k].getDOUBLE())
						return true;
				else if(attribute.isSetURI()&& attrList[k].isSetURI() &&
						attribute.getURI().equals(attrList[k].getURI()))
						return true;
				else if(attribute.isSetSIGNATURE()&& attrList[k].isSetSIGNATURE() &&
						attribute.getSIGNATURE() == attrList[k].getSIGNATURE())
						return true;
				else if(attribute.isSetKEY()&& attrList[k].isSetKEY() &&
						attribute.getKEY() == attrList[k].getKEY())
						return true;
				else if(attribute.isSetDATETIME()&& attrList[k].isSetDATETIME() &&
						attribute.getDATETIME().equals(attrList[k].getDATETIME()))
						return true;
				else if(attribute.isSetDURATION()&& attrList[k].isSetDURATION() &&
						attribute.getDURATION().equals(attrList[k].getDURATION()))
						return true;
					
		}
		return false;
	}

	@XmppMethod(XmppMethod.POST)
    @Consumes(value = OcciXml.MEDIA_TYPE, serializer = OcciXml.class)
    @Produces(value = UriText.MEDIA_TYPE, serializer = UriText.class)
	public UriText addSlaTemplate(OcciXml template) {
		// create a virtual machine and return its uri
		TemplateInstance templateInst = new TemplateInstance(template);
		String path = this.addResource(templateInst);
		try {
			UriText uri = new UriText(path);
			return uri;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new UriText(); 
		}
	}

}