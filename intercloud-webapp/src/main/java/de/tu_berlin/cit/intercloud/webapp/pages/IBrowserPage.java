package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.client.model.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.request.component.IRequestablePage;

import java.util.List;

public interface IBrowserPage extends IRequestablePage {
    void browse(String jid, String restPath);

    void selectMethod(MethodModel methodModel);
    void executeMethod(MethodModel methodModel, IRepresentationModel representationModel);

    default void selectAction(ActionModel actionModel) { }
    default void executeAction(ActionModel actionModel, List<ParameterModel> parameterModelList) {}
}
