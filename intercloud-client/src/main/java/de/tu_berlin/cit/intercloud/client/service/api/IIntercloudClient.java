package de.tu_berlin.cit.intercloud.client.service.api;

import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;

import java.util.List;

/**
 * The interface is associated with an {@link de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument}.
 * It provides methods to perform operations on based on the underlying
 * {@link de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument}.
 */
public interface IIntercloudClient {

    /**
     * @return A {@link LoggingModel} used to log the received XWADL,
     * the send XML-REST and received XML-REST.
     */
    LoggingModel getLoggingModel();

    /*
        METHODs
     */

    /**
     * @return The Method Models of the underlying XWADL.
     */
    List<MethodModel> getMethods();

    /**
     * Creates an {@link IRepresentationModel} based on the request media type of
     * the specified {@link MethodModel}.
     * @param methodModel
     * @return
     * @throws Exception
     */
    IRepresentationModel getRepresentationModel(MethodModel methodModel) throws Exception;

    /**
     * Executes a Method and returns the result.
     * Creates a {@link de.tu_berlin.cit.rwx4j.rest.RestDocument} based on the specified
     * {@link MethodModel} and {@link IRepresentationModel} and sends it to the corresponding
     * XMPP entity.
     * The representation of response is converted, depending on the Method's response media type,
     * into an {@link IRepresentationModel} and returned.
     * @param methodModel The Method to be executed.
     * @param requestRepresentationModel The Representation Model which is set in the requesting XML-REST document.
     * @return
     * @throws Exception
     */
    IRepresentationModel executeMethod(MethodModel methodModel, IRepresentationModel requestRepresentationModel)
            throws Exception;

    /*
        ACTIONs
     */

    /**
     * @return The Action Models of the underlying XWADL.
     */
    List<ActionModel> getActions();

    /**
     * Creates the {@link ParameterModel}s based on the specified {@link ActionModel}.
     * @param actionModel
     * @return
     */
    List<ParameterModel> getParameters(ActionModel actionModel);

    /**
     * Executes an Action and returns the result.
     * Creates a {@link de.tu_berlin.cit.rwx4j.rest.RestDocument} based on the specified
     * {@link ActionModel} and {@link ParameterModel}s and sends it to the corresponding
     * XMPP entity.
     * The result of the response is converted into a {@link ParameterModel} and returned.
     * @param actionModel The Action to be executed.
     * @param parameterModelList The Parameters of the Action to be executed.
     * @return
     * @throws Exception
     */
    ParameterModel executeAction(ActionModel actionModel, List<ParameterModel> parameterModelList)
            throws Exception;
}
