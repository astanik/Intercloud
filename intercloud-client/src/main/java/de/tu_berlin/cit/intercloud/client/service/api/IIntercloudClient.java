package de.tu_berlin.cit.intercloud.client.service.api;

import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;

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
