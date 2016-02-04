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
import de.tu_berlin.cit.intercloud.webapp.panels.request.OcciRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.OcciResponsePanel;
import de.tu_berlin.cit.intercloud.webapp.panels.response.UriResponsePanel;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.rwx4j.XmppURI;
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

    private Code newCodePanel(String markupId) {
        Code code = new Code(markupId);
        code.setLanguage(CodeBehavior.Language.XML);
        code.setOutputMarkupId(true);
        return code;
    }

    private void requestXwadl(String entity, String path) {
        try {
            XmppURI uri = new XmppURI(entity, path);
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService().newIntercloudClient(uri);
            this.xwadlCode.setDefaultModel(Model.of(intercloudClient.toString()));
            this.methodModelList = intercloudClient.getMethods();
            this.requestForm.setMethodModel(null);
            this.requestForm.setRepresentationModel(null);
            this.responseContainer.setRepresentationModel(null);
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logger.error("Failed to request xwadl from entity: {}, path: {}", entity, path);
            this.xwadlCode.setDefaultModel(Model.of());
            this.methodModelList = null;
            logError(e);
        }
    }

    private void requestXwadl(String entity, String path, AjaxRequestTarget target) {
        requestXwadl(entity, path);
        target.add(this.xwadlCode, this.methodTable, this.alert, this.requestForm, this.responseContainer);
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
                        try {
                            // execute method directly if no request media type is given
                            AbstractRepresentationModel representation = IntercloudWebSession.get().getIntercloudService()
                                    .getIntercloudClient(methodModel.getUri())
                                    .executeMethod(null, methodModel);
                            // display response
                            BrowserPage.this.responseContainer.setRepresentationModel(representation);
                            // hide request
                            BrowserPage.this.requestForm.setMethodModel(null);
                            BrowserPage.this.requestForm.setRepresentationModel(null);
                            ComponentUtils.displayNone(alert);
                        } catch (Exception e) {
                            logError(e);
                            logger.error("Failed to execute request. {}", methodModel, e);
                        }
                    } else {
                        // get request representation model
                        try {
                            AbstractRepresentationModel representation = IntercloudWebSession.get().getIntercloudService()
                                    .getIntercloudClient(methodModel.getUri())
                                    .getRepresentationModel(methodModel);
                            // display request
                            BrowserPage.this.requestForm.setMethodModel(methodModel);
                            BrowserPage.this.requestForm.setRepresentationModel(representation);
                            // hide response
                            BrowserPage.this.responseContainer.setRepresentationModel(null);
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
                    logger.info(representationModel.getObject().toString());
                    try {
                        AbstractRepresentationModel representationModel = IntercloudWebSession.get().getIntercloudService()
                                .getIntercloudClient(methodModel.getObject().getUri())
                                .executeMethod(RequestForm.this.representationModel.getObject(), methodModel.getObject());
                        logger.info(representationModel.toString());

                        BrowserPage.this.responseContainer.setRepresentationModel(representationModel);
                        target.add(BrowserPage.this.responseContainer);
                        BrowserPage.this.requestForm.setMethodModel(null);
                        BrowserPage.this.requestForm.setRepresentationModel(null);
                        target.add(BrowserPage.this.requestForm);
                        target.add(ComponentUtils.displayNone(BrowserPage.this.alert));
                    } catch (Exception e) {
                        target.add(BrowserPage.this.logError(e));
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    super.onError(target, form);
                }
            });
            this.add(new EmptyPanel("requestPanel"));
            ComponentUtils.displayNone(this);
        }

        public void setMethodModel(MethodModel methodModel) {
            this.methodModel.setObject(methodModel);
        }

        public void setRepresentationModel(AbstractRepresentationModel representationModel) {
            this.representationModel.setObject(representationModel);
        }

        @Override
        protected void onBeforeRender() {
            AbstractRepresentationModel representation = this.representationModel.getObject();
            if (null != this.methodModel.getObject() && representation instanceof OcciRepresentationModel) {
                this.replace(new OcciRequestPanel("requestPanel", methodModel, Model.of((OcciRepresentationModel) representation)));
                ComponentUtils.displayBlock(this);
            } else {
                this.replace(new EmptyPanel("requestPanel"));
                ComponentUtils.displayNone(this);
            }

            super.onBeforeRender();
        }
    }

    private class ResponseContainer extends WebMarkupContainer {
        private final Model<AbstractRepresentationModel> representationModel = Model.of();

        public ResponseContainer(String markupId) {
            super(markupId);

            this.add(new EmptyPanel("responsePanel"));
        }

        public void setRepresentationModel(AbstractRepresentationModel representationModel) {
            this.representationModel.setObject(representationModel);
        }

        @Override
        protected void onBeforeRender() {
            AbstractRepresentationModel representation = this.representationModel.getObject();
            if (representation instanceof UriRepresentationModel) {
                restCode.setDefaultModel(new Model<>());
                this.replace(new UriResponsePanel("responsePanel",
                        Model.of(new UriListRepresentationModel(((UriRepresentationModel) representation).getUri()))));
            } else if (representation instanceof UriListRepresentationModel) {
                restCode.setDefaultModel(new Model<>());
                this.replace(new UriResponsePanel("responsePanel",
                        Model.of((UriListRepresentationModel) representation)));
            } else if (representation instanceof TextRepresentationModel) {
                this.replace(new Label("responsePanel", Model.of(((TextRepresentationModel) representation).getText())));
                restCode.setDefaultModel(Model.of(((TextRepresentationModel) representation).getText()));
            } else if (representation instanceof OcciRepresentationModel) {
                this.replace(new OcciResponsePanel("responsePanel",
                        Model.of((OcciRepresentationModel) representation)));
            } else {
                this.replace(new EmptyPanel("responsePanel"));
            }

            super.onBeforeRender();
        }
    }
}
