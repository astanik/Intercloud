package de.tu_berlin.cit.intercloud.client.service;

import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.MethodModel;

import java.util.List;

public interface IIntercloudClient {

    LoggingModel getLoggingModel();

    /*
        METHODs
     */

    List<MethodModel> getMethods();

    IRepresentationModel getRepresentationModel(MethodModel methodModel) throws Exception;

    IRepresentationModel executeMethod(MethodModel methodModel, IRepresentationModel requestRepresentationModel)
            throws Exception;

    /*
        ACTIONs
     */

    List<ActionModel> getActions();

    List<ParameterModel> getParameters(ActionModel actionModel);

    ParameterModel executeAction(ActionModel actionModel, List<ParameterModel> parameterModelList)
            throws Exception;
}
