package de.tu_berlin.cit.intercloud.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.CodeBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.client.model.rest.AbstractRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.TextRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriRepresentationModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.components.ComponentUtils;
import de.tu_berlin.cit.intercloud.webapp.panels.OcciRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.UriResponsePanel;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

import java.util.List;

public class BrowserPage extends UserTemplate {
    private final Logger logger = LoggerFactory.getLogger(BrowserPage.class);
    private final String ID_REQUEST_PANEL = "requestPanel";
    private final String ID_RESPONSE_PANEL = "responsePanel";

    private final IModel<String> entity;
    // ajax components
    private final MethodTable methodTable;
    private final Alert alert;
    private final Code xwadlCode;
    private final Code restCode;

    // serialize (relevant for browser history)
    private IModel<String> resourcePath;

    // don't serialize (not relevant for browser history)
    private transient List<MethodModel> methodModelList; //= new ArrayList<>();

    public BrowserPage(IModel<XmppURI> uri) {
        super();
        this.entity = new Model<>(uri.getObject().getJID());
        this.resourcePath = new Model<>(uri.getObject().getPath());

        // alert
        this.alert = newAlert("alert");
        this.add(this.alert);
        this.add(new XwadlForm("xwadlForm"));

        // Code section, kind of debugging
        this.xwadlCode = newCodePanel("xwadlCode");
        this.add(this.xwadlCode);
        this.restCode = newCodePanel("restCode");
        this.add(this.restCode);

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

        // request xwadl
        if (!resourcePath.getObject().trim().isEmpty()) {
            requestXwadl(entity.getObject(), resourcePath.getObject(), null);
        }
    }

    private Alert newAlert(String markupId) {
        Alert alert = new Alert(markupId, Model.of(), Model.of());
        alert.type(Alert.Type.Danger);
        alert.setOutputMarkupId(true);
        return ComponentUtils.displayNone(alert);
    }

    private Alert logAlert(Throwable t) {
        alert.withHeader(Model.of(ExceptionUtils.getMessage(t)));
        alert.withMessage(Model.of(ExceptionUtils.getStackTrace(t)));
        return ComponentUtils.displayBlock(alert);
    }

    private Code newCodePanel(String markupId) {
        Code code = new Code(markupId);
        code.setLanguage(CodeBehavior.Language.XML);
        code.setOutputMarkupId(true);
        return code;
    }

    private void requestXwadl(String entity, String path, AjaxRequestTarget target) {
        try {
            XmppURI uri = new XmppURI(entity, path);
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService().newIntercloudClient(uri);
            this.xwadlCode.setDefaultModel(Model.of(intercloudClient.toString()));
            this.methodModelList = intercloudClient.getMethods();
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logger.error("Failed to request xwadl from entity: {}, path: {}", entity, path);
            this.xwadlCode.setDefaultModel(Model.of());
            this.methodModelList = null;
            logAlert(e);
        }

        if (null != target) {
            target.add(this.xwadlCode, this.methodTable, this.alert);
        }
    }

    private class XwadlForm extends Form {

        public XwadlForm(String markupId) {
            super(markupId);

            this.add(new Label("domain", BrowserPage.this.entity));
            this.add(new TextField<>("resourcePath", resourcePath).setRequired(true));
            AjaxButton button = new AjaxButton("getXwadlBtn") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    BrowserPage.this.requestXwadl(entity.getObject(), resourcePath.getObject(), target);
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
                        if (null == methodModel.getRequestMediaType()) {
                            AbstractRepresentationModel representation = IntercloudWebSession.get().getIntercloudService()
                                    .getIntercloudClient(methodModel.getUri())
                                    .executeMethod(null, methodModel);
                            if (representation instanceof UriRepresentationModel) {
                                restCode.setDefaultModel(new Model<>());
                                BrowserPage.this.replace(new UriResponsePanel(ID_RESPONSE_PANEL,
                                        new Model<>(new UriListRepresentationModel(((UriRepresentationModel) representation).getUri()))));
                            } else if (representation instanceof UriListRepresentationModel) {
                                restCode.setDefaultModel(new Model<>());
                                BrowserPage.this.replace(new UriResponsePanel(ID_RESPONSE_PANEL,
                                        new Model<>((UriListRepresentationModel) representation)));
                            } else if (representation instanceof TextRepresentationModel) {
                                restCode.setDefaultModel(new Model<>(((TextRepresentationModel) representation).getText()));
                            }
                        }
                        ComponentUtils.displayNone(alert);
                    } catch (Exception e) {
                        logAlert(e);
                        logger.error("Failed to execute request. {}", methodModel, e);
                    }

                    try {
                        if ("xml/occi".equals(methodModel.getRequestMediaType())) {
                            AbstractRepresentationModel representationModel = IntercloudWebSession.get().getIntercloudService()
                                    .getIntercloudClient(methodModel.getUri())
                                    .getRepresentationModel(methodModel);
                            BrowserPage.this.replace(new OcciRequestPanel(ID_REQUEST_PANEL,
                                    new Model<>(methodModel),
                                    new Model<>((OcciRepresentationModel) representationModel)));
                        }
                        ComponentUtils.displayNone(alert);
                    } catch (Exception e) {
                        logAlert(e);
                        logger.error("Could not create occi request model.", e);
                    }
                }
            }.setBody(Model.of(methodModel.getMethodType()));

            if (null == methodModel.getRequestMediaType()) {
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
