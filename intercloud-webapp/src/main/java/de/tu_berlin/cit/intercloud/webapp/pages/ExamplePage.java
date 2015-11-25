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
            attributeList.add(new AttributeModel("Datetime", true, AttributeModel.Type.DATETIME.toString(), "This is an Example for a Datetime AttributeModel."));
            attributeList.add(new AttributeModel("Integer", false, AttributeModel.Type.INTEGER.toString(), null));
            attributeList.add(new AttributeModel("Double", false, AttributeModel.Type.DOUBLE.toString(), "This is an Example for a Double AttributeModel."));
            attributeList.add(new AttributeModel("Float", false, AttributeModel.Type.FLOAT.toString(), "This is an Example for a Float AttributeModel."));
            attributeList.add(new AttributeModel("Boolean", false, AttributeModel.Type.BOOLEAN.toString(), "This is an Example for a Boolean AttributeModel."));
            attributeList.add(new AttributeModel("String", true, AttributeModel.Type.STRING.toString(), "This is an Example for a String AttributeModel."));
            attributeList.add(new AttributeModel("Enum", false, AttributeModel.Type.ENUM.toString(), "This is an Example for an Enum AttributeModel."));
            attributeList.add(new AttributeModel("Uri", false, AttributeModel.Type.URI.toString(), "This is an Example for an URI AttributeModel."));

            return attributeList;
        }
    }
}
