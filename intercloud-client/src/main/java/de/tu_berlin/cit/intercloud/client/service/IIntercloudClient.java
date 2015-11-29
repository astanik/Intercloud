package de.tu_berlin.cit.intercloud.client.service;

import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.RequestModel;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.List;

public interface IIntercloudClient {
    List<MethodModel> getMethods();

    RequestModel getRequestModel(MethodModel methodModel);

    CategoryModel applyTemplate(CategoryModel categoryModel, MethodModel methodModel, String templateTitle);

    String executeRequest(RequestModel requestModel, MethodModel methodModel) throws XMPPException, IOException, SmackException;
}
