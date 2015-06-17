package de.tu_berlin.cit.intercloud.xmpp.rest;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.RequestDocument.Request;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument.ResourceType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResponseDocument.Response;


public class ResourceContainer extends ResourceInstance {

	
	public ResourceContainer(XmppURI uri) {
		super(uri.toString());
	}
	
	public ResourceTypeDocument getXWADL(String path) {
		ResourceTypeDocument xwadl = ResourceTypeDocument.Factory.newInstance();
		ResourceType resType = xwadl.addNewResourceType();
		resType.setPath(path);
		// search instance
		ResourceInstance instance = this.getResource(path);
		if(instance == null)
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Resource not found");
		
		// search methods
		for(java.lang.reflect.Method method : instance.getClass().getMethods()) {
			// create method entry
			if(method.isAnnotationPresent(XmppMethod.class))
				this.createMethodXWADL(method, resType.addNewMethod());
		}
		
		return xwadl;
	}

	private void createMethodXWADL(
			java.lang.reflect.Method method,
			de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method xmlMethod) {
		// set method type
		xmlMethod.setType(MethodType.Enum.forString(method.getAnnotation(XmppMethod.class).value()));
		
		// add request information
		if(method.isAnnotationPresent(Consumes.class)) {
			Request xmlRequest = xmlMethod.addNewRequest();
			xmlRequest.setMediaType(method.getAnnotation(Consumes.class).value());
			Class<? extends Representation> serializer = method.getAnnotation(Consumes.class).serializer();
			try {
				List<Representation> templates = serializer.newInstance().getTemplates();
				if(templates != null) {
					for(Representation rep : templates) {
						xmlRequest.addNewTemplate().setStringValue(rep.toString());
					}
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// add response information
		if(method.isAnnotationPresent(Produces.class)) {
			Response xmlResponse = xmlMethod.addNewResponse();
			xmlResponse.setMediaType(method.getAnnotation(Produces.class).value());
		}
	}

	public ResourceDocument execute(ResourceDocument xmlRequest) {
		// create request document
		ResourceDocument xmlResponse = (ResourceDocument) xmlRequest.copy();
		String path = xmlRequest.getResource().getPath();
		// search instance
		ResourceInstance instance = this.getResource(path);
		if(instance == null)
			throw new RuntimeException("Failed: ResourceContainer: "
					+ "Resource not found");
		// select method
//		Method method = xmlRequest.getResource().getMethod();
//		String name = method.getName();
//		instance.getClass().getM
		// TODO Auto-generated method stub
		return xmlResponse;
	}

	
	
	
}
