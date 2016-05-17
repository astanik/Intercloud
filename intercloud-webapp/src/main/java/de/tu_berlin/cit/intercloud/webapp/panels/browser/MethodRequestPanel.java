package de.tu_berlin.cit.intercloud.webapp.panels.browser;

import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;
import de.tu_berlin.cit.intercloud.webapp.pages.IBrowserPage;
import de.tu_berlin.cit.intercloud.webapp.panels.plugin.api.IRepresentationPanelPlugin;
import de.tu_berlin.cit.intercloud.webapp.panels.plugin.impl.RepresentationPanelRegistry;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class MethodRequestPanel extends Panel {
    private IModel<IRepresentationModel> representationModel;
    private Form requestForm;
    private Panel requestPanel;

    public MethodRequestPanel(String markupId,
                              IModel<MethodModel> methodModel,
                              IModel<IRepresentationModel> representationModel,
                              IBrowserPage browserPage) {
        super(markupId);
        this.representationModel = representationModel;

        this.requestForm = new Form("requestForm");
        this.requestForm.add(new AjaxSubmitLink("requestSubmit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                browserPage.executeMethod(methodModel.getObject(), representationModel.getObject());
                this.setResponsePage(browserPage);

            }
        });

        this.requestForm.add(new EmptyPanel("requestPanel"));
        this.add(this.requestForm);
    }

    @Override
    protected void onConfigure() {
        IRepresentationModel representation = this.representationModel.getObject();
        IRepresentationPanelPlugin panelPlugin = RepresentationPanelRegistry.getInstance().getPlugin(representation);
        if (null != panelPlugin) {
            this.requestPanel = panelPlugin.getRequestPanel("requestPanel", representation);
        } else {
            this.requestPanel = null;
        }

        super.onConfigure();
    }

    @Override
    protected void onBeforeRender() {
        if (null != this.requestPanel) {
            this.requestForm.replace(this.requestPanel);
        }

        super.onBeforeRender();
    }

    @Override
    public boolean isVisible() {
        return null != this.requestPanel;
    }
}
