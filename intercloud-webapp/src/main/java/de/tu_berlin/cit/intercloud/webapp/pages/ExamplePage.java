package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.panels.attribute.AttributeInputPanel;
import de.tu_berlin.cit.intercloud.webapp.template.Template;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ExamplePage extends Template {
    private static final Logger logger = LoggerFactory.getLogger(ExamplePage.class);

    public ExamplePage() {
        super();

        this.add(new AttributeForm("attributeForm"));
    }

    private class AttributeForm extends Form {
        private List<AttributeModel> attributeList;

        public AttributeForm(String markupId) {
            super(markupId);
            this.attributeList = createExampleAttributeList();

            this.add(new AttributeInputPanel("attributePanel", new LoadableDetachableModel<List<AttributeModel>>() {
                @Override
                protected List<AttributeModel> load() {
                    return attributeList;
                }
            }));
            this.add(new AjaxButton("attributeSubmit") {
                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.appendJavaScript("alert('Please fill out all REQUIRED fields!');");
                }

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    for (AttributeModel a : attributeList) {
                        logger.info(a.toString());
                    }
                }
            });
        }

        private List<AttributeModel> createExampleAttributeList() {
            List<AttributeModel> attributeList = new ArrayList<>();
            attributeList.add(new AttributeModel("Datetime", AttributeModel.Type.DATETIME.toString(), true, true, "This is an Example for a Datetime AttributeModel."));
            attributeList.add(new AttributeModel("Integer", AttributeModel.Type.INTEGER.toString(), false, true, null));
            attributeList.add(new AttributeModel("Double", AttributeModel.Type.DOUBLE.toString(), false, true, "This is an Example for a Double AttributeModel."));
            attributeList.add(new AttributeModel("Float", AttributeModel.Type.FLOAT.toString(), false, true, "This is an Example for a Float AttributeModel."));
            attributeList.add(new AttributeModel("Boolean", AttributeModel.Type.BOOLEAN.toString(), false, true, "This is an Example for a Boolean AttributeModel."));
            attributeList.add(new AttributeModel("String", AttributeModel.Type.STRING.toString(), true, true, "This is an Example for a String AttributeModel."));
            attributeList.add(new AttributeModel("Enum", AttributeModel.Type.ENUM.toString(), false, true, "This is an Example for an Enum AttributeModel."));
            attributeList.add(new AttributeModel("Uri", AttributeModel.Type.URI.toString(), false, true, "This is an Example for an URI AttributeModel."));

            AttributeModel attributeModel = new AttributeModel("Show Immutable with value", AttributeModel.Type.STRING.toString(), false, false, "This is an immutable attribute with some value.");
            attributeModel.setString("should be visible and disabled");
            attributeList.add(attributeModel);

            attributeList.add(new AttributeModel("Hide Immutable with no value", AttributeModel.Type.STRING.toString(), true, false, "This Example should not be displayed."));


            return attributeList;
        }
    }
}