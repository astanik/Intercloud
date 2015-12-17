package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.client.model.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriListRepresentationModel;
import de.tu_berlin.cit.intercloud.webapp.panels.request.KindRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.UriResponsePanel;
import de.tu_berlin.cit.intercloud.webapp.panels.request.attribute.AttributeInputPanel;
import de.tu_berlin.cit.intercloud.webapp.template.Template;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExamplePage extends Template {
    private static final Logger logger = LoggerFactory.getLogger(ExamplePage.class);

    public ExamplePage() {
        super();

        this.add(new AttributeForm("attributeForm"));
        this.add(new KindRequestPanel("kindPanel",
                new Model<>(new MethodModel(null, null, null, null, null)),
                new LoadableDetachableModel<KindModel>() {
                    @Override
                    protected KindModel load() {
                        return createExampleKindModel();
                    }
                }));
        this.add(new UriResponsePanel("uriResponsePanel", new LoadableDetachableModel<UriListRepresentationModel>() {
            @Override
            protected UriListRepresentationModel load() {
                return createEmapmleUriRepresentationModel();
            }
        }));

    }

    private class AttributeForm extends Form {
        private List<AttributeModel> attributeList;

        public AttributeForm(String markupId) {
            super(markupId);
            this.attributeList = createExampleAttributeList();

            final FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            this.add(feedback);
            this.add(new AttributeInputPanel("attributePanel", new ListModel<>(this.attributeList)));
            this.add(new AjaxButton("attributeSubmit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    for (AttributeModel a : attributeList) {
                        logger.info(a.toString());
                    }
                    target.add(feedback);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            });
        }

        private List<AttributeModel> createExampleAttributeList() {
            List<AttributeModel> attributeList = new ArrayList<>();
            attributeList.add(new AttributeModel("Datetime", AttributeModel.Type.DATETIME, true, true, "This is an Example for a Datetime AttributeModel."));
            attributeList.add(new AttributeModel("Integer", AttributeModel.Type.INTEGER, false, true, null));
            attributeList.add(new AttributeModel("Double", AttributeModel.Type.DOUBLE, false, true, "This is an Example for a Double AttributeModel."));
            attributeList.add(new AttributeModel("Float", AttributeModel.Type.FLOAT, false, true, "This is an Example for a Float AttributeModel."));
            attributeList.add(new AttributeModel("Boolean", AttributeModel.Type.BOOLEAN, false, true, "This is an Example for a Boolean AttributeModel."));
            attributeList.add(new AttributeModel("String", AttributeModel.Type.STRING, true, true, "This is an Example for a String AttributeModel."));
            attributeList.add(new AttributeModel("Enum", AttributeModel.Type.ENUM, false, true, "This is an Example for an Enum AttributeModel."));
            attributeList.add(new AttributeModel("Uri", AttributeModel.Type.URI, false, true, "This is an Example for an URI AttributeModel."));
            attributeList.add(new AttributeModel("List", AttributeModel.Type.LIST, false, true, null));
            attributeList.add(new AttributeModel("Map", AttributeModel.Type.MAP, false, true, null));
            attributeList.add(new AttributeModel("Duration", AttributeModel.Type.DURATION, false, true, null));
            attributeList.add(new AttributeModel("Key", AttributeModel.Type.KEY, false, true, null));
            attributeList.add(new AttributeModel("Signature", AttributeModel.Type.SIGNATURE, false, true, null));

            AttributeModel attributeModel = new AttributeModel("Show Immutable with value", AttributeModel.Type.STRING, false, false, "This is an immutable attribute with some value.");
            attributeModel.setString("should be visible and disabled");
            attributeList.add(attributeModel);

            attributeList.add(new AttributeModel("Hide Immutable with no value", AttributeModel.Type.STRING, true, false, "This Example should not be displayed."));


            return attributeList;
        }
    }


    public KindModel createExampleKindModel() {
        KindModel kindModel = new KindModel("http://schema.ogf.org/occi/infrastructure#", "compute");
        kindModel.addTemplate("t0");
        kindModel.addTemplate("t1");
        kindModel.addAttribute(new AttributeModel("Datetime", AttributeModel.Type.DATETIME, false, true, null));
        kindModel.addAttribute(new AttributeModel("String", AttributeModel.Type.STRING, true, true, null));
        return kindModel;
    }

    public UriListRepresentationModel createEmapmleUriRepresentationModel() {
        UriListRepresentationModel representationModel = new UriListRepresentationModel();
        representationModel.setUriList(Arrays.asList("xmpp://john@doe.de/asdf#/path", "xmpp://example.component.de/asdf#/path0/path1"));
        return representationModel;
    }
}
