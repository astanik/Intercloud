package de.tu_berlin.cit.intercloud.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.CodeBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.rest.AbstractRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.OcciRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.TextRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriListRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.UriRepresentationModel;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.components.ComponentUtils;
import de.tu_berlin.cit.intercloud.webapp.panels.request.OcciRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.OcciListResponsePanel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.OcciResponsePanel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.UriResponsePanel;
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

    private final IModel<String> entity;
    // ajax components
    private final MethodTable methodTable;
    private final RequestForm requestForm;
    private final ResponseContainer responseContainer;
    private final Alert alert;
    private final Code xwadlCode;
    private final Code restCode;

    // serialize (relevant for browser history)
    private IModel<String> resourcePath;

    // don't serialize (not relevant for browser history)
    private transient List<MethodModel> methodModelList;
    private transient LoggingModel loggingModel;

    public BrowserPage(IModel<XmppURI> uri) {
        super();
        this.entity = new Model<>(uri.getObject().getJID());
        this.resourcePath = new Model<>(uri.getObject().getPath());

        // alert
        this.alert = newAlert("alert");
        this.add(this.alert);
        this.add(new XwadlForm("xwadlForm"));

        // Code section, kind of debugging
        this.xwadlCode = newCodePanel("xwadlCode", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return null != loggingModel ? loggingModel.getXwadlDocument() : null;
            }
        });
        this.add(this.xwadlCode);
        this.restCode = newCodePanel("restCode", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return null != loggingModel ? loggingModel.getRestDocument() : null;
            }
        });
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
        this.requestForm = new RequestForm("requestForm");
        this.requestForm.setOutputMarkupId(true);
        this.add(this.requestForm);
        this.responseContainer = new ResponseContainer("responseContainer");
        this.responseContainer.setOutputMarkupId(true);
        this.add(this.responseContainer);

        // request xwadl
        if (!resourcePath.getObject().trim().isEmpty()) {
            requestXwadl(entity.getObject(), resourcePath.getObject());
        }
    }

    private Alert newAlert(String markupId) {
        Alert alert = new Alert(markupId, Model.of(), Model.of());
        alert.type(Alert.Type.Danger);
        alert.setOutputMarkupId(true);
        return ComponentUtils.displayNone(alert);
    }

    private Alert logError(Throwable t) {
        alert.type(Alert.Type.Danger);
        alert.withHeader(Model.of(ExceptionUtils.getMessage(t)));
        alert.withMessage(Model.of(ExceptionUtils.getStackTrace(t)));
        return ComponentUtils.displayBlock(alert);
    }

    private Code newCodePanel(String markupId, IModel<String> model) {
        Code code = new Code(markupId, model);
        code.setLanguage(CodeBehavior.Language.XML);
        code.setOutputMarkupId(true);
        return code;
    }

    private void requestXwadl(String entity, String path) {
        try {
            XmppURI uri = new XmppURI(entity, path);
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService().newIntercloudClient(uri);
            this.loggingModel = intercloudClient.getLoggingModel();
            this.methodModelList = intercloudClient.getMethods();
            this.requestForm.setModel(null, null);
            this.responseContainer.setModel(null);
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logger.error("Failed to request xwadl from entity: {}, path: {}", entity, path);
            this.methodModelList = null;
            logError(e);
        }
    }

    private void requestXwadl(String entity, String path, AjaxRequestTarget target) {
        requestXwadl(entity, path);
        target.add(this.xwadlCode, this.methodTable, this.alert, this.requestForm, this.responseContainer);
    }

    private void executeMethod(AbstractRepresentationModel representationModel, MethodModel methodModel) {
        try {
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService()
                    .getIntercloudClient(methodModel.getUri());
            this.loggingModel = intercloudClient.getLoggingModel();
            AbstractRepresentationModel representation = intercloudClient.executeMethod(representationModel, methodModel);
            // display response
            this.responseContainer.setModel(representation);
            // hide request
            this.requestForm.setModel(null, null);
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logError(e);
            logger.error("Failed to execute request. {}", methodModel, e);
        }
    }

    private void executeMethod(AbstractRepresentationModel representationModel, MethodModel methodModel, AjaxRequestTarget target) {
        executeMethod(representationModel, methodModel);
        target.add(this.responseContainer, this.requestForm, this.alert);
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
                    if (null == methodModel.getRequestMediaType()) {
                        // execute method directly if no request media type is given
                        BrowserPage.this.executeMethod(null, methodModel);
                    } else {
                        // get request representation model
                        try {
                            AbstractRepresentationModel representation = IntercloudWebSession.get().getIntercloudService()
                                    .getIntercloudClient(methodModel.getUri())
                                    .getRepresentationModel(methodModel);
                            // display request
                            BrowserPage.this.requestForm.setModel(representation, methodModel);
                            // hide response
                            BrowserPage.this.responseContainer.setModel(null);
                            ComponentUtils.displayNone(alert);
                        } catch (Exception e) {
                            logError(e);
                            logger.error("Could not create request model.", e);
                        }
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

    private class RequestForm extends Form {
        private final Model<MethodModel> methodModel = Model.of();
        private final Model<AbstractRepresentationModel> representationModel = Model.of();

        public RequestForm(String markupId) {
            super(markupId);

            this.add(new AjaxButton("requestSubmit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    BrowserPage.this.executeMethod(representationModel.getObject(), methodModel.getObject(), target);
                }
            });

            this.add(new EmptyPanel("requestPanel"));
            ComponentUtils.displayNone(this);
        }

        public void setModel(AbstractRepresentationModel representation, MethodModel method) {
            this.representationModel.setObject(representation);
            this.methodModel.setObject(method);

            if (null != method && representation instanceof OcciRepresentationModel) {
                this.replace(new OcciRequestPanel("requestPanel", methodModel, Model.of((OcciRepresentationModel) representation)));
                ComponentUtils.displayBlock(this);
            } else {
                this.replace(new EmptyPanel("requestPanel"));
                ComponentUtils.displayNone(this);
            }
        }
    }

    private class ResponseContainer extends WebMarkupContainer {
        private final Model<AbstractRepresentationModel> representationModel = Model.of();

        public ResponseContainer(String markupId) {
            super(markupId);

            this.add(new EmptyPanel("responsePanel"));
        }

        public void setModel(AbstractRepresentationModel representation) {
            this.representationModel.setObject(representation);

            if (representation instanceof UriRepresentationModel) {
                this.replace(new UriResponsePanel("responsePanel",
                        Model.of(new UriListRepresentationModel(((UriRepresentationModel) representation).getUri()))));
            } else if (representation instanceof UriListRepresentationModel) {
                this.replace(new UriResponsePanel("responsePanel",
                        Model.of((UriListRepresentationModel) representation)));
            } else if (representation instanceof TextRepresentationModel) {
                this.replace(new Label("responsePanel",
                        Model.of(((TextRepresentationModel) representation).getText())));
            } else if (representation instanceof OcciRepresentationModel) {
                this.replace(new OcciResponsePanel("responsePanel",
                        Model.of((OcciRepresentationModel) representation)));
            } else if (representation instanceof OcciListRepresentationModel) {
                this.replace(new OcciListResponsePanel("responsePanel",
                        Model.of((OcciListRepresentationModel) representation)));
            } else {
                this.replace(new EmptyPanel("responsePanel"));
            }
        }
    }
}
