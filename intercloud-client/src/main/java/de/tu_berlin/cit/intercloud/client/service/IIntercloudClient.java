package de.tu_berlin.cit.intercloud.client.service;

import de.tu_berlin.cit.intercloud.client.exception.AttributeFormatException;
import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.exception.ParameterFormatException;
import de.tu_berlin.cit.intercloud.client.exception.UnsupportedMethodException;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.AbstractRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.TemplateModel;
import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.List;

public interface IIntercloudClient {

    LoggingModel getLoggingModel();

    /*
        METHODs
     */

    List<MethodModel> getMethods();

    AbstractRepresentationModel getRepresentationModel(MethodModel methodModel) throws UnsupportedMethodException, MissingClassificationException;

    CategoryModel applyTemplate(CategoryModel categoryModel, TemplateModel templateModel);

    AbstractRepresentationModel executeMethod(MethodModel methodModel, AbstractRepresentationModel requestRepresentationModel)
            throws XMPPException, IOException, SmackException, UnsupportedMethodException, AttributeFormatException, XmlException;

    /*
        ACTIONs
     */

    List<ActionModel> getActions();

    List<ParameterModel> getParameters(ActionModel actionModel);

    ParameterModel executeAction(ActionModel actionModel, List<ParameterModel> parameterModelList)
            throws ParameterFormatException, XMPPException, IOException, SmackException;
}
