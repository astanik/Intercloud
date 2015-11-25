package de.tu_berlin.cit.intercloud.client.service.impl;

import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.RequestModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.occi.client.OcciClient;
import de.tu_berlin.cit.intercloud.occi.client.OcciMethodInvocation;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IntercloudClient implements IIntercloudClient {
    private final IXmppService xmppService;
    private final OcciClient occiClient;
    private final XmppURI uri;

    public IntercloudClient(IXmppService xmppService, ResourceTypeDocument xwadl, XmppURI uri) {
        this.xmppService = xmppService;
        this.occiClient = new OcciClient(xwadl);
        this.uri = uri;
    }

    @Override
    public List<MethodModel> getMethods() {
        List<MethodModel> result = new ArrayList<>();
        MethodDocument.Method[] methodArray = occiClient.getResourceTypeDocument().getResourceType().getMethodArray();
        if (null != methodArray) {
            for (MethodDocument.Method m : methodArray) {
                String requestMediaType = null;
                String responseMediaType = null;
                String documentation = null;
                if (m.isSetRequest()) {
                    requestMediaType = m.getRequest().getMediaType();
                }
                if (m.isSetResponse()) {
                    responseMediaType = m.getResponse().getMediaType();
                }
                if (null != m.getDocumentation()) {
                    documentation = m.getDocumentation().getStringValue();
                }
                result.add(new MethodModel(m.getType().toString(), requestMediaType, responseMediaType, documentation));
            }
        }
        return result;
    }

    @Override
    public RequestModel getRequestModel(MethodModel methodModel) {
        return null;
    }

    @Override
    public String executeRequest(RequestModel requestModel, MethodModel methodModel) throws XMPPException, IOException, SmackException {
        MethodDocument.Method method = occiClient.getMethod(
                MethodType.Enum.forString(methodModel.getMethodType()), methodModel.getRequestMediaType(), methodModel.getResponseMediaType());
        if (null == requestModel && !methodModel.hasRequest()) {
            OcciMethodInvocation methodInvocation = occiClient.buildMethodInvocation(method);
            ResourceDocument response = xmppService.sendRestDocument(this.uri, methodInvocation.getXmlDocument());
            return response.toString();
        }

        throw new UnsupportedOperationException("Method not supported. " + methodModel);
    }

    @Override
    public String toString() {
        return occiClient.getResourceTypeDocument().toString();
    }
}
