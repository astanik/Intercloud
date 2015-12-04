package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.client.model.occi.CategoryModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.occi.MixinModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.panels.attribute.AttributeInputPanel;
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

public class CategoryRequestPanel extends Panel {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequestPanel.class);
    private final WebMarkupContainer container;

    public CategoryRequestPanel(String markupId, IModel<MethodModel> methodModel, IModel<? extends CategoryModel> categoryModel) {
        super(markupId);

        this.container = new WebMarkupContainer("container");
        this.container.setOutputMarkupId(true);
        this.add(this.container);

        CategoryModel category = categoryModel.getObject();
        Label typeLabel = new Label("type");
        if (category instanceof KindModel) {
            typeLabel.setDefaultModel(Model.of("Kind"));
        } else if (category instanceof MixinModel) {
            typeLabel.setDefaultModel(Model.of("Mixin"));
        } else if (category instanceof LinkModel) {
            typeLabel.setDefaultModel(Model.of("Link"));
        }
        this.container.add(typeLabel);
        this.container.add(new Label("term", new PropertyModel<>(category, "term")));
        this.container.add(new Label("schema", new PropertyModel<>(category, "schema")));
        this.container.add(new Label("title", new PropertyModel<>(category, "title")));
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
        this.container.add(templateChoice);
    }

}
