package de.tu_berlin.cit.intercloud.client.service;

import de.tu_berlin.cit.intercloud.client.exception.AttributeFormatException;
import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.exception.UnsupportedMethodException;
import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.rest.AbstractRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.List;

public interface IIntercloudClient {
    List<MethodModel> getMethods();

    AbstractRepresentationModel getRepresentationModel(MethodModel methodModel) throws UnsupportedMethodException, MissingClassificationException;

    CategoryModel applyTemplate(CategoryModel categoryModel, MethodModel methodModel, String templateTitle) throws UnsupportedMethodException;

    AbstractRepresentationModel executeMethod(AbstractRepresentationModel requestRepresentationModel, MethodModel methodModel)
            throws XMPPException, IOException, SmackException, UnsupportedMethodException, AttributeFormatException, XmlException;

    // TODO executeAction
}
