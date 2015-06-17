package de.tu_berlin.cit.intercloud.xmpp.rest;

import java.util.ArrayList;
import java.util.List;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.OcciText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.PlainText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriListText;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.UriText;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType.Enum;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public class ResourceClient {

	private final ResourceTypeDocument doc;
	
	public ResourceClient(ResourceTypeDocument doc) {
		this.doc = doc;
	}

	public List<Method> getMethods(Enum type) {
		Method[] xmlMethods = this.doc.getResourceType().getMethodArray();
		ArrayList<Method> list = new ArrayList<Method>();
		for(int i=0; i<xmlMethods.length; i++)
			if(xmlMethods[i].getType().toString().equals(type.toString()))
				list.add(xmlMethods[i]);
		
		return list;
	}
	
	public MethodInvocation buildMethodInvocation(Method method) {
		ResourceDocument resourceDoc = createBasicResourceDocument();
		return new MethodInvocation(resourceDoc, method);
	}

	private ResourceDocument createBasicResourceDocument() {
		ResourceDocument resourceDoc = ResourceDocument.Factory.newInstance();
		resourceDoc.addNewResource().setPath(this.doc.getResourceType().getPath());
		return resourceDoc;
	}
	
	public Class<? extends Representation> getRequestRepresentationClass(Method method) {
		if(method.isSetRequest()) {
			String mediaType = method.getRequest().getMediaType();
			if(mediaType.equals(PlainText.MEDIA_TYPE)) {
				return PlainText.class;
			} else if(mediaType.equals(UriText.MEDIA_TYPE)) {
				return UriText.class;
			} else if(mediaType.equals(UriListText.MEDIA_TYPE)) {
				return UriListText.class;
			} else if(mediaType.equals(OcciText.MEDIA_TYPE)) {
				return OcciText.class;
			}
		}
		return null;
	}
	
	public Representation getRequestRepresentation(Method method) {
		Class<? extends Representation> repClass = getRequestRepresentationClass(method);
		Representation rep = null;
		try {
			if(repClass != null)
				rep = repClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rep;
	}
	
	public List<Representation> getRequestTemplates(Method method) {
		List<Representation> templates = new ArrayList<Representation>();
		if(method.isSetRequest()) {
			String[] tmpl = method.getRequest().getTemplateArray();
			for(int i=0; i<tmpl.length; i++) {
				Representation rep = getRequestRepresentation(method);
				rep.readRepresentation(tmpl[i]);
				templates.add(rep);
			}
		}
		return templates;
	}
	
}
