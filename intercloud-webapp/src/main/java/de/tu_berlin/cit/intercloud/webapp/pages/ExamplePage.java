package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.webapp.panels.AttributeInputPanel;
import de.tu_berlin.cit.intercloud.webapp.template.Template;
import de.tu_berlin.cit.intercloud.xmpp.client.occi.representation.Attribute;
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
        private List<Attribute> attributeList;

        public AttributeForm(String markupId) {
            super(markupId);
            this.attributeList = createExampleAttributeList();

            this.add(new AttributeInputPanel("attributePanel", new LoadableDetachableModel<List<Attribute>>() {
                @Override
                protected List<Attribute> load() {
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
                    for (Attribute a : attributeList) {
                           logger.info(a.toString());
                    }
                }
            });
        }

        private List<Attribute> createExampleAttributeList() {
            List<Attribute> attributeList = new ArrayList<>();
            attributeList.add(new Attribute("Datetime", true, Attribute.Type.DATETIME.toString(), "This is an Example for a Datetime Attribute."));
            attributeList.add(new Attribute("Integer", false, Attribute.Type.INTEGER.toString(), "This is an Example for an Integer Attribute."));
            attributeList.add(new Attribute("Double", false, Attribute.Type.DOUBLE.toString(), "This is an Example for a Double Attribute."));
            attributeList.add(new Attribute("Float", false, Attribute.Type.FLOAT.toString(), "This is an Example for a Float Attribute."));
            attributeList.add(new Attribute("Boolean", false, Attribute.Type.BOOLEAN.toString(), "This is an Example for a Boolean Attribute."));
            attributeList.add(new Attribute("String", true, Attribute.Type.STRING.toString(), "This is an Example for a String Attribute."));
            attributeList.add(new Attribute("Enum", false, Attribute.Type.ENUM.toString(), "This is an Example for an Enum Attribute."));

            return attributeList;
        }
    }
}
