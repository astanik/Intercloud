package de.tu_berlin.cit.intercloud.webapp.panels.method.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.convert.TemplateHelper;
import de.tu_berlin.cit.intercloud.client.model.method.TemplateModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
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

    public CategoryRequestPanel(String markupId, IModel<? extends CategoryModel> categoryModel) {
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
        this.container.add(new AttributeListInputPanel("attributePanel", new ListModel<>(new ArrayList<>(category.getAttributes()))));

        DropDownChoice<TemplateModel> templateChoice = new DropDownChoice<>("templates", new Model<>(),
                category.getTemplates(), new ChoiceRenderer<>("name"));
        templateChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                TemplateModel template = templateChoice.getModelObject();
                try {
                    TemplateHelper.applyTemplate(categoryModel.getObject(), template);
                } catch (Exception e) {
                    logger.error("Could apply template. title: {}", template.getName(), e);
                    target.appendJavaScript("alert('Failed to apply template.');");
                }
                target.add(container);
            }
        });
        //templateChoice.setNullValid(true); // keep null to be selectable
        this.container.add(new WebMarkupContainer("templatesRow") {
            @Override
            public boolean isVisible() {
                return 1 < category.getTemplates().size();
            }
        }.add(templateChoice));
    }

    public WebMarkupContainer getContainer() {
        return container;
    }

    public abstract String getType();
}
