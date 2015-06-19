package de.tu_berlin.cit.intercloud.xmpp.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Consumes;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Produces;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.RequestDocument.Request;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument.ResourceType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResponseDocument.Response;

public class XwadlBuilder {

	protected final static Logger logger = LoggerFactory.getLogger(XwadlBuilder.class);

	public static ResourceTypeDocument build(String path,
			ResourceInstance instance) {
//		logger.info("Start building xwadl document");
		// create new document 
		ResourceTypeDocument xwadl = ResourceTypeDocument.Factory.newInstance();
		// set resource path
		ResourceType resType = xwadl.addNewResourceType();
		resType.setPath(path);
//		logger.info("resource path=" + path);
		// search methods
		for(java.lang.reflect.Method method : instance.getClass().getMethods()) {
			// create method entry
			if(method.isAnnotationPresent(XmppMethod.class))
				createMethodXWADL(method, resType.addNewMethod());
		}
		// TODO Actions
		
//		logger.info("Finished building xwadl document");
//		logger.info("New xwadl document: " + xwadl.toString());
		return xwadl;
	}

	private static void createMethodXWADL(
			java.lang.reflect.Method method,
			de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument.Method xmlMethod) {
		// set method type
		xmlMethod.setType(MethodType.Enum.forString(method.getAnnotation(XmppMethod.class).value()));
//		logger.info("method=" + method.getName() + " has annotation XmppMethod with value=" + xmlMethod.getType().toString());
		
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

}
