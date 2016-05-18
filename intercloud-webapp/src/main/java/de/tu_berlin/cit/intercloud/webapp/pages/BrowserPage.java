package de.tu_berlin.cit.intercloud.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import de.tu_berlin.cit.intercloud.client.model.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModel;
import de.tu_berlin.cit.intercloud.client.profiling.api.IProfilingCommand;
import de.tu_berlin.cit.intercloud.client.profiling.impl.ProfilingService;
import de.tu_berlin.cit.intercloud.client.service.api.IIntercloudClient;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.components.ComponentUtils;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.BreadcrumbPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.LoggingPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.ActionRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.ActionTablePanel;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.AddressBarPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.MethodRequestPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.browser.MethodTablePanel;
import de.tu_berlin.cit.intercloud.webapp.panels.plugin.api.IRepresentationPanelPlugin;
import de.tu_berlin.cit.intercloud.webapp.panels.plugin.impl.RepresentationPanelRegistry;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;

/**
 * This page is inspired by an Internet browser.
 * It is used to browse REST with XMPP resources.
 * The page contains the following components:
 * <ul>
 * <li>{@link BreadcrumbPanel}
 * displays the current path of a resource and provides links to navigate through the path.
 * </li>
 * <li>{@link AddressBarPanel}
 * similar to the Internet browser's address bar, it is used to enter a resource path.
 * </li>
 * <li>{@link Alert}
 * provides a Bootstrap alert in order to display exceptions in form of the stacktrace.
 * </li>
 * <li>{@link MethodTablePanel}
 * shows the available Methods offered by the current resource.
 * </li>
 * <li>{@link ActionTablePanel}</li>
 * shows the available Actions offered by the current resource.
 * <li>{@code browserPanel}</li>
 * acts as a container to show either representations or parameters.
 * </ul>
 */
public class BrowserPage extends UserTemplate implements IBrowserPage {
    private static final Logger logger = LoggerFactory.getLogger(BrowserPage.class);

    private Alert alert;

    private IModel<XmppURI> uri;
    private ListModel<MethodModel> methodModelList = new ListModel<>();
    private ListModel<ActionModel> actionModelList = new ListModel<>();
    private IModel<LoggingModel> loggingModel = Model.of();

    public BrowserPage(IModel<XmppURI> uri) {
        super();
        this.uri = uri;

        this.add(new BreadcrumbPanel("breadcrumb", uri, (XmppURI redirectUri) -> new BrowserPage(Model.of(redirectUri))));

        // alert
        this.alert = newAlert("alert");
        this.add(this.alert);
        this.add(new AddressBarPanel("addressBarPanel", this.uri, this));

        // Code section, kind of debugging
        this.add(new LoggingPanel("loggingPanel", this.loggingModel));

        // method table
        this.add(new MethodTablePanel("methodPanel", methodModelList, this));

        // action table
        this.add(new ActionTablePanel("actionPanel", actionModelList, this));

        // request / response
        this.add(new EmptyPanel("browserPanel"));

        // request xwadl
        if (!uri.getObject().getPath().trim().isEmpty()) {
            browse();
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

    /**
     * Displays the stack trace of an exception in the alert panel.
     * @param t
     * @return
     */
    private Alert logError(Throwable t) {
        alert.type(Alert.Type.Danger);
        alert.withHeader(Model.of(ExceptionUtils.getMessage(t)));
        alert.withMessage(Model.of(ExceptionUtils.getStackTrace(t)));
        return ComponentUtils.displayBlock(alert);
    }

    @Override
    public void browse(String jid, String restPath) {
        try {
            this.uri.setObject(new XmppURI(jid, restPath));
            browse();
            this.replace(new EmptyPanel("browserPanel"));
        } catch (URISyntaxException e) {
            logger.error("Failed to set Uri. Path \"{}\"is not supported. ", restPath, e);
            logError(e);
        }
    }

    private void browse() {
        try {
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService()
                    .newIntercloudClient(this.uri.getObject());
            this.loggingModel.setObject(intercloudClient.getLoggingModel());
            this.methodModelList.setObject(intercloudClient.getMethods());
            this.actionModelList.setObject(intercloudClient.getActions());
            ComponentUtils.displayNone(this.alert);
        } catch (Exception e) {
            logger.error("Failed to request xwadl from {}", uri, e);
            this.methodModelList.setObject(null);
            this.actionModelList.setObject(null);
            logError(e);
        }
    }

    @Override
    public void selectMethod(MethodModel methodModel) {
        if (null == methodModel.getRequestMediaType()) {
            // execute method directly if no request media type is given
            this.executeMethod(methodModel, null);
        } else {
            try {
                // get request representation model
                IRepresentationModel representation = IntercloudWebSession.get().getIntercloudService()
                        .getIntercloudClient(methodModel.getUri())
                        .getRepresentationModel(methodModel);
                // display request model
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
            // execute method
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
            // hide alert
            ComponentUtils.displayNone(this.alert);
        } catch (Throwable t) {
            logError(t);
            logger.error("Failed to execute request. method: {}, representation: {}", methodModel, requestRepresentation, t);
        }
    }

    @Override
    public void selectAction(ActionModel actionModel) {
        try {
            // get parameters
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService().getIntercloudClient(this.uri.getObject());
            List<ParameterModel> parameters = intercloudClient.getParameters(actionModel);
            if (null == parameters || parameters.isEmpty()) {
                // execute action directly if no parameters are given
                this.executeAction(actionModel, parameters);
            } else {
                // display parameters
                this.replace(new ActionRequestPanel("browserPanel",
                        Model.of(actionModel), new ListModel<>(parameters), this));
                // hide alert
                ComponentUtils.displayNone(alert);
            }
        } catch (Throwable t) {
            logError(t);
            logger.error("Failed to select action. {}", actionModel, t);
        }
    }

    @Override
    public void executeAction(ActionModel actionModel, List<ParameterModel> parameterModelList) {
        try {
            // execute action
            IIntercloudClient intercloudClient = IntercloudWebSession.get().getIntercloudService().getIntercloudClient(this.uri.getObject());
            ParameterModel parameterModel = intercloudClient.executeAction(actionModel, parameterModelList);
            // display result
            this.replace(new Label("browserPanel", parameterModel));
            // hide alert
            ComponentUtils.displayNone(alert);
            // finally re-browse the resource, since the action may changed its state
            //browse();
        } catch (Throwable t) {
            logError(t);
            logger.error("Failed to execute action. {}, {}", actionModel, parameterModelList, t);
        }
    }
}
