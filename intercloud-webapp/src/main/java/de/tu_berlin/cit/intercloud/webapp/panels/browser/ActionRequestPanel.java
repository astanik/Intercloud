package de.tu_berlin.cit.intercloud.webapp.panels.browser;

import de.tu_berlin.cit.intercloud.client.model.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.webapp.pages.IBrowserPage;
import de.tu_berlin.cit.intercloud.webapp.panels.action.ParameterListInputPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

/**
 * This panel displays the {@link ParameterModel}s of an {@link ActionModel}
 * in order to perform a request.
 * It provides a button to invoke {@link IBrowserPage#executeAction(ActionModel, List)}
 * of the {@link IBrowserPage} containing this panel.
 */
public class ActionRequestPanel extends Panel {

    public ActionRequestPanel(String markupId,
                              IModel<ActionModel> actionModel,
                              IModel<List<ParameterModel>> parameterModels,
                              IBrowserPage browserPage) {
        super(markupId);

        Form requestForm = new Form("requestForm");
        requestForm.add(new AjaxSubmitLink("requestSubmit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                browserPage.executeAction(actionModel.getObject(), parameterModels.getObject());
                this.setResponsePage(browserPage);

            }
        }.setBody(Model.of(actionModel.getObject().getName())));

        requestForm.add(new ParameterListInputPanel("parameterList", parameterModels));
        this.add(requestForm);
    }
}
