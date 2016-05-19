package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.client.model.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.request.component.IRequestablePage;

import java.util.List;

/**
 * The interface provides methods to browse REST with XMPP
 * resources of an XMPP entity.
 * These methods may be invoked by other Wicket components,
 * that are part of the page implementing this interface.
 */
public interface IBrowserPage extends IRequestablePage {
    /**
     * Browses a REST with XMPP resource. Thus, this method
     * receives an {@link de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument}
     * for a specified XMPP URI and renders its Methods and Actions.
     * @param jid The Jabber ID of the XMPP entity where to receive the XWADL from.
     * @param restPath The Path of the REST with XMPP resource of a specified XMPP entity.
     */
    void browse(String jid, String restPath);

    /**
     * Selects a specific {@link MethodModel} and displays its
     * {@link IRepresentationModel}.
     * @param methodModel
     */
    void selectMethod(MethodModel methodModel);

    /**
     * Executes a specific {@link MethodModel} with its {@link IRepresentationModel}
     * and displays the resulting {@link IRepresentationModel}.
     * @param methodModel
     * @param representationModel
     */
    void executeMethod(MethodModel methodModel, IRepresentationModel representationModel);

    /**
     * Selects a specific {@link ActionModel} and display its
     * {@link ParameterModel}s.
     * @param actionModel
     */
    void selectAction(ActionModel actionModel);

    /**
     * Executes a specific {@link ActionModel} with its {@link ParameterModel}s
     * and displays the resulting {@link ParameterModel}.
     * @param actionModel
     * @param parameterModelList
     */
    void executeAction(ActionModel actionModel, List<ParameterModel> parameterModelList);
}
