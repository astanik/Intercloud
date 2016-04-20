package de.tu_berlin.cit.intercloud.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModel;
import de.tu_berlin.cit.intercloud.client.model.rest.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.profiling.IProfilingTask;
import de.tu_berlin.cit.intercloud.client.profiling.ProfilingService;
import de.tu_berlin.cit.intercloud.client.service.IIntercloudClient;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.components.ComponentUtils;
import de.tu_berlin.cit.intercloud.webapp.panels.BreadcrumbPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.LoggingPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.plugin.IRepresentationPanelPlugin;
import de.tu_berlin.cit.intercloud.webapp.panels.plugin.RepresentationPanelRegistry;
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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;

public class BrowserPage extends UserTemplate {
    private static final Logger logger = LoggerFactory.getLogger(BrowserPage.class);

    private MethodTable methodTable;
    private RequestForm requestForm;
    private ResponseContainer responseContainer;
    private Alert alert;

    private IModel<XmppURI> uri;
    private ListModel<MethodModel> methodModelList = new ListModel<>();
    private IModel<LoggingModel> loggingModel = Model.of();

    public BrowserPage(IModel<XmppURI> uri) {
        super();
        this.uri = uri;

        this.add(new BreadcrumbPanel("breadcrumb", uri, Model.of((XmppURI redirectUri) -> new BrowserPage(Model.of(redirectUri)))));

        // alert
        this.alert = newAlert("alert");
        this.add(this.alert);
        this.add(new XwadlForm("xwadlForm"));

        // Code section, kind of debugging
        this.add(new LoggingPanel("loggingPanel", this.loggingModel));

        // method table
        this.methodTable = new MethodTable("methodTable", methodModelList);
        this.add(methodTable);

        // request / response
        this.requestForm = new RequestForm("requestForm");
        this.add(this.requestForm);
        this.responseContainer = new ResponseContainer("responseContainer");
        this.add(this.responseContainer);

        // request xwadl

        if (!uri.getObject().getPath().trim().isEmpty()) {
            requestXwadl(uri.getObject());
        }
    }

    @Override
    protected void onRender() {
        ProfilingService.getInstance().invokeAndProfile(
                new IProfilingTask() {
                    @Override
                    public String getIdentifier() {
                        return "render";
                    }

                    @Override
                    public Object invoke() {
                        BrowserPage.super.onRender();
                        return null;
                    }
                }
        );
    }

    @Override
    protected void onBeforeRender() {
        ProfilingService.getInstance().invokeAndProfile(
                new IProfilingTask() {
                    @Override
                    public String getIdentifier() {
                        return "beforeRender";
                    }

                    @Override
                    public Object invoke() {
                        BrowserPage.super.onBeforeRender();
                        return null;
                    }
                }
        );
    }

    @Override
    protected void onConfigure() {
        ProfilingService.getInstance().invokeAndProfile(
                new IProfilingTask() {
                    @Override
                    public String getIdentifier() {
                        return "configure";
                    }

                    @Override
                    public Object invoke() {
                        BrowserPage.super.onConfigure();
                        return null;
                    }
                }
        );
    }

    private Alert newAlert(String markupId) {
        Alert alert = new Alert(markupId, Model.of(), Model.of());
        alert.type(Alert.Type.Danger);
        return ComponentUtils.displayNone(alert);
    }

    private Alert logError(Throwable t) {
        alert.type(Alert.Type.Danger);
        alert.withHeader(Model.of(ExceptionUtils.getMessage(t)));
        alert.withMessage(Model.of(ExceptionUtils.getStackTrace(t)));
        return ComponentUtils.displayBlock(alert);
    }

    private void requestXwadl(String path) {
        try {
            XmppURI oldUri = this.uri.getObject();
            this.uri.setObject(new XmppURI(oldUri.getJID(), path));

            requestXwadl(this.uri.getObject());
        } catch (URISyntaxException e) {
            logger.error("Failed to set Uri. Path \"{}\"is not supported. ", path, e);
            logError(e);
        }
    }

    private void requestXwadl(XmppURI uri) {
        try {
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService().newIntercloudClient(uri);
            this.loggingModel.setObject(intercloudClient.getLoggingModel());
            this.methodModelList.setObject(intercloudClient.getMethods());
            this.requestForm.setModel(null, null);
            this.responseContainer.setModel(null);
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logger.error("Failed to request xwadl from {}", uri, e);
            this.methodModelList.setObject(null);
            logError(e);
        }
    }

    private void executeMethod(IRepresentationModel representationModel, MethodModel methodModel) {
        try {
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService()
                    .getIntercloudClient(methodModel.getUri());
            this.loggingModel.setObject(intercloudClient.getLoggingModel());
            IRepresentationModel representation = intercloudClient.executeMethod(methodModel, representationModel);
            // display response
            this.responseContainer.setModel(representation);
            // hide request
            this.requestForm.setModel(null, null);
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logError(e);
            logger.error("Failed to execute request. method: {}, representation: {}", methodModel, representationModel, e);
        }
    }

    private class XwadlForm extends Form {
        private String resourcePath = BrowserPage.this.uri.getObject().getPath();

        public XwadlForm(String markupId) {
            super(markupId);

            this.add(new Label("domain", BrowserPage.this.uri.getObject().getJID()));
            this.add(new TextField<>("resourcePath", new PropertyModel(this, "resourcePath")).setRequired(true));
            AjaxButton button = new AjaxButton("getXwadlBtn") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    BrowserPage.this.requestXwadl(resourcePath);
                    setResponsePage(BrowserPage.this);
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
                    listItem.add(newLink("methodLink", methodModel));
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
                            IRepresentationModel representation = IntercloudWebSession.get().getIntercloudService()
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
            }.setBody(Model.of(methodModel.getType()));

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

    public class RequestForm extends Form {
        private Model<MethodModel> methodModel = Model.of();
        private Model<IRepresentationModel> representationModel = Model.of();
        private Panel requestPanel = null;

        public RequestForm(String markupId) {
            super(markupId);

            this.add(new AjaxButton("requestSubmit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    BrowserPage.this.executeMethod(representationModel.getObject(), methodModel.getObject());
                    setResponsePage(BrowserPage.this);
                }
            });

            this.add(new EmptyPanel("requestPanel"));
        }

        @Override
        protected void onConfigure() {
            IRepresentationModel representation = representationModel.getObject();
            IRepresentationPanelPlugin panelPlugin = RepresentationPanelRegistry.getInstance().getPlugin(representation);
            if (null != panelPlugin) {
                this.requestPanel = panelPlugin.getRequestPanel("requestPanel", representation);
            } else {
                this.requestPanel = null;
            }

            super.onConfigure();
        }

        @Override
        protected void onBeforeRender() {
            if (null != this.requestPanel) {
                this.replace(this.requestPanel);
            }

            super.onBeforeRender();
        }

        @Override
        public boolean isVisible() {
            return null != this.requestPanel;
        }

        public void setModel(IRepresentationModel representation, MethodModel method) {
            this.representationModel.setObject(representation);
            this.methodModel.setObject(method);
        }
    }

    private class ResponseContainer extends WebMarkupContainer {
        private Model<IRepresentationModel> representationModel = Model.of();
        private Panel responsePanel = null;

        public ResponseContainer(String markupId) {
            super(markupId);

            this.add(new EmptyPanel("responsePanel"));
        }

        @Override
        protected void onConfigure() {
            IRepresentationModel representation = representationModel.getObject();
            IRepresentationPanelPlugin panelPlugin = RepresentationPanelRegistry.getInstance().getPlugin(representation);
            if (null != panelPlugin) {
                this.responsePanel = panelPlugin.getResponsePanel("responsePanel", representation);
            } else {
                this.responsePanel = null;
            }

            super.onConfigure();
        }

        @Override
        protected void onBeforeRender() {
            if (null != this.responsePanel) {
                this.replace(this.responsePanel);
            }

            super.onBeforeRender();
        }

        @Override
        public boolean isVisible() {
            return null != this.responsePanel;
        }

        public void setModel(IRepresentationModel representation) {
            this.representationModel.setObject(representation);
        }
    }
}
