package de.tu_berlin.cit.intercloud.webapp.panels.request;

import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.panels.request.attribute.AttributeInputPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public abstract class CategoryRequestPanel extends Panel {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequestPanel.class);
    private final WebMarkupContainer container;

    public CategoryRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<? extends CategoryModel> categoryModel) {
        super(markupId);

        this.container = new WebMarkupContainer("container");
        this.container.setOutputMarkupId(true);
        this.add(this.container);

        CategoryModel category = categoryModel.getObject();
        this.container.add(new Label("type", getType()));
        this.container.add(new Label("term", category.getTerm()));
        this.container.add(new Label("schema", category.getSchema()));
        PropertyModel<String> title = new PropertyModel<>(category, "title");
        this.container.add(new WebMarkupContainer("titleRow") {
            @Override
            public boolean isVisible() {
                return null != title.getObject();
            }
        }.add(new Label("title", title)));
        this.container.add(new AttributeInputPanel("attributePanel", new ListModel<>(new ArrayList<>(category.getAttributes()))));

        DropDownChoice<String> templateChoice = new DropDownChoice<>("templates", new Model<>(), new ArrayList<>(category.getTemplates()));
        templateChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                MethodModel method = methodModel.getObject();
                String templateTitle = templateChoice.getModelObject();
                try {
                    IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService().getIntercloudClient(method.getUri());
                    intercloudClient.applyTemplate(categoryModel.getObject(), method, templateTitle);
                } catch (Exception e) {
                    logger.error("Could apply template. title: {}, method: {}", templateTitle, method, e);
                    target.appendJavaScript("alert('Failed to apply template.');");
                }
                target.add(container);
            }
        });
        templateChoice.setNullValid(true); // keep null to be selectable
        this.container.add(new WebMarkupContainer("templatesRow") {
            @Override
            public boolean isVisible() {
                return !category.getTemplates().isEmpty();
            }
        }.add(templateChoice));
    }

    public WebMarkupContainer getContainer() {
        return container;
    }

    public abstract String getType();
}
