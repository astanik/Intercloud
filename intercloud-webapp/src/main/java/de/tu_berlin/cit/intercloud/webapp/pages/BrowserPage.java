package de.tu_berlin.cit.intercloud.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;
import de.tu_berlin.cit.intercloud.client.profiling.api.IProfilingCommand;
import de.tu_berlin.cit.intercloud.client.profiling.impl.ProfilingService;
import de.tu_berlin.cit.intercloud.client.service.api.IIntercloudClient;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.components.ComponentUtils;
import de.tu_berlin.cit.intercloud.webapp.panels.BreadcrumbPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.LoggingPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.MethodPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.MethodRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.plugin.IRepresentationPanelPlugin;
import de.tu_berlin.cit.intercloud.webapp.panels.plugin.RepresentationPanelRegistry;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class BrowserPage extends UserTemplate implements IBrowserPage {
    private static final Logger logger = LoggerFactory.getLogger(BrowserPage.class);

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
        this.add(new MethodPanel("methodPanel", methodModelList, this));

        // request / response
        this.add(new EmptyPanel("browserPanel"));

        // request xwadl
        if (!uri.getObject().getPath().trim().isEmpty()) {
            requestXwadl(uri.getObject());
        }
    }

    @Override
    protected void onRender() {
        ProfilingService.getInstance().execute(
                new IProfilingCommand() {
                    @Override
                    public String getIdentifier() {
                        return "render";
                    }

                    @Override
                    public Object execute() {
                        BrowserPage.super.onRender();
                        return null;
                    }
                }
        );
    }

    @Override
    protected void onBeforeRender() {
        ProfilingService.getInstance().execute(
                new IProfilingCommand() {
                    @Override
                    public String getIdentifier() {
                        return "beforeRender";
                    }

                    @Override
                    public Object execute() {
                        BrowserPage.super.onBeforeRender();
                        return null;
                    }
                }
        );
    }

    @Override
    protected void onConfigure() {
        ProfilingService.getInstance().execute(
                new IProfilingCommand() {
                    @Override
                    public String getIdentifier() {
                        return "configure";
                    }

                    @Override
                    public Object execute() {
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
            this.replace(new EmptyPanel("browserPanel"));
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logger.error("Failed to request xwadl from {}", uri, e);
            this.methodModelList.setObject(null);
            logError(e);
        }
    }

    @Override
    public void selectMethod(MethodModel methodModel) {
        if (null == methodModel.getRequestMediaType()) {
            // execute method directly if no request media type is given
            this.executeMethod(methodModel, null);
        } else {
            // get request representation model
            try {
                IRepresentationModel representation = IntercloudWebSession.get().getIntercloudService()
                        .getIntercloudClient(methodModel.getUri())
                        .getRepresentationModel(methodModel);
                // display request
                this.replace(new MethodRequestPanel("browserPanel",
                        Model.of(methodModel), Model.of(representation), BrowserPage.this));
                // hide alert
                ComponentUtils.displayNone(alert);
            } catch (Throwable t) {
                logError(t);
                logger.error("Could not create request model.", t);
            }
        }
    }

    @Override
    public void executeMethod(MethodModel methodModel, IRepresentationModel requestRepresentation) {
        try {
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService()
                    .getIntercloudClient(methodModel.getUri());
            this.loggingModel.setObject(intercloudClient.getLoggingModel());
            IRepresentationModel responseRepresentation = intercloudClient.executeMethod(methodModel, requestRepresentation);
            // display response
            IRepresentationPanelPlugin plugin = RepresentationPanelRegistry.getInstance().getPlugin(responseRepresentation);
            if (null != plugin) {
                this.replace(plugin.getResponsePanel("browserPanel", responseRepresentation));
            } else {
                this.replace(new EmptyPanel("browserPanel"));
            }
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logError(e);
            logger.error("Failed to execute request. method: {}, representation: {}", methodModel, requestRepresentation, e);
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
}
