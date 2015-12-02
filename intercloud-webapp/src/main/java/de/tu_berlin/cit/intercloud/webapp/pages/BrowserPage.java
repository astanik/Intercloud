package de.tu_berlin.cit.intercloud.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.CodeBehavior;
import de.tu_berlin.cit.intercloud.client.model.rest.AbstractRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.RequestModel;
import de.tu_berlin.cit.intercloud.client.model.rest.TextRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriRepresentationModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.panels.OcciRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.UriResponsePanel;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BrowserPage extends UserTemplate {
    private final Logger logger = LoggerFactory.getLogger(BrowserPage.class);
    private final String ID_REQUEST_PANEL = "requestPanel";
    private final String ID_RESPONSE_PANEL = "responsePanel";

    private final IModel<String> entity;
    // ajax components
    private final Code codePanel;
    private final MethodTable methodTable;

    // serialize (relevant for browser history)
    private IModel<String> resourcePath;

    // don't serialize (not relevant for browser history)
    private transient String codeString;
    private transient List<MethodModel> methodModelList = new ArrayList<>();

    public BrowserPage(IModel<XmppURI> uri) {
        super();
        this.entity = new Model<>(uri.getObject().getJID());
        this.resourcePath = new Model<>(uri.getObject().getPath());

        this.add(new XwadlForm("xwadlForm"));

        // Code section, kind of debugging
        this.codePanel = new Code("code", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return codeString;
            }
        });
        this.codePanel.setLanguage(CodeBehavior.Language.XML);
        this.codePanel.setOutputMarkupId(true);
        this.add(this.codePanel);

        // method table
        this.methodTable = new MethodTable("methodTable", new LoadableDetachableModel<List<MethodModel>>() {
            @Override
            protected List<MethodModel> load() {
                return methodModelList;
            }
        });
        this.methodTable.setOutputMarkupId(true);
        this.add(methodTable);

        // request / response
        this.add(new EmptyPanel(ID_REQUEST_PANEL));
        this.add(new EmptyPanel(ID_RESPONSE_PANEL));
    }

    private class XwadlForm extends Form {

        public XwadlForm(String markupId) {
            super(markupId);

            this.add(new Label("domain", BrowserPage.this.entity));
            this.add(new TextField<>("resourcePath", resourcePath).setRequired(true));
            AjaxButton button = new AjaxButton("getXwadlBtn") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    try {
                        XmppURI uri = new XmppURI(entity.getObject(), resourcePath.getObject());
                        IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService()
                                .newIntercloudClient(uri);
                        codeString = intercloudClient.toString();
                        methodModelList = intercloudClient.getMethods();
                        target.add(codePanel);
                        target.add(methodTable);
                    } catch (Exception e) {
                        StringBuilder s = new StringBuilder();
                        s.append("Failed to receive xwadl from xmpp://").append(entity.getObject()).append("#").append(resourcePath.getObject()).append(".");
                        logger.error(s.toString(), e);
                        target.appendJavaScript(s.insert(0, "alert('").append("');").toString());
                        return;
                    }
                }
            };
            this.add(button);
            this.setDefaultButton(button);
        }
    }

    private class MethodTable extends WebMarkupContainer {

        public MethodTable(String id, IModel<List<MethodModel>> methodList) {
            super(id);

            this.add(new ListView<MethodModel>("methodList", methodList) {
                @Override
                protected void populateItem(ListItem<MethodModel> listItem) {
                    MethodModel methodModel = listItem.getModelObject();
                    listItem.add(newLink("method", methodModel));
                    listItem.add(newLabel("documentation", methodModel.getDocumentation()));
                    listItem.add(newLabel("request", methodModel.getRequestMediaType()));
                    listItem.add(newLabel("response", methodModel.getResponseMediaType()));
                }
            });
        }

        private Label newLabel(String markupId, String s) {
            Label label = new Label(markupId);
            if (null != s) {
                label.setDefaultModel(Model.of(s));
            }
            return label;
        }

        private AbstractLink newLink(String markupId, MethodModel methodModel) {
            AbstractLink link = new Link(markupId) {
                @Override
                public void onClick() {
                    try {
                        if (!methodModel.hasRequest()) {
                            AbstractRepresentationModel representation = IntercloudWebSession.get().getIntercloudService()
                                    .getIntercloudClient(methodModel.getUri())
                                    .executeRequest(null, methodModel);
                            if (representation instanceof UriRepresentationModel) {
                                codeString = null;
                                BrowserPage.this.replace(new UriResponsePanel(ID_RESPONSE_PANEL, new Model<>((UriRepresentationModel) representation)));
                            } else if (representation instanceof TextRepresentationModel){
                                codeString = ((TextRepresentationModel) representation).getText();
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Failed to execute request. {}", methodModel, e);
                    }

                    try {
                        if ("xml/occi".equals(methodModel.getRequestMediaType())) {
                            RequestModel requestModel = IntercloudWebSession.get().getIntercloudService()
                                    .getIntercloudClient(methodModel.getUri())
                                    .getRequestModel(methodModel);
                            BrowserPage.this.replace(new OcciRequestPanel(ID_REQUEST_PANEL, new Model<>(methodModel), new Model<>(requestModel)));
                        }
                    } catch (Exception e) {
                        logger.error("Could not create occi request model.", e);
                    }
                }
            }.setBody(Model.of(methodModel.getMethodType()));

            if (!methodModel.hasRequest()) {
                link.add(new AttributeAppender("class", " btn-success"));
            } else if ("xml/occi".equals(methodModel.getRequestMediaType())) {
                link.add(new AttributeAppender("class", " btn-info"));
            } else {
                link.setEnabled(false);
                link.add(new AttributeAppender("class", " disables"));
            }
            return link;
        }
    }
}
