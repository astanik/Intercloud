package de.tu_berlin.cit.intercloud.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.CodeBehavior;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.panels.KindPanel;
import de.tu_berlin.cit.intercloud.webapp.panels.MethodTablePanel;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetXwadlPage extends UserTemplate {
    private final Logger logger = LoggerFactory.getLogger(GetXwadlPage.class);

    private final IModel<String> domainModel;
    private final Code xwadlCodePanel;
    private ResourceTypeDocument xwadl;

    private final MethodTablePanel methodTablePanel;
    private final KindPanel kindPanel;

    public GetXwadlPage(IModel<String> domainModel) {
        super();
        this.domainModel = domainModel;

        this.add(new XwadlForm("xwadlForm"));

        // Code section, kind of debugging
        this.xwadlCodePanel = new Code("xwadlCode", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return xwadl == null ? "" : xwadl.toString();
            }
        });
        this.xwadlCodePanel.setLanguage(CodeBehavior.Language.XML);
        this.xwadlCodePanel.setOutputMarkupId(true);
        this.add(this.xwadlCodePanel);

        // method table
        this.methodTablePanel = new MethodTablePanel("methodTablePanel");
        this.methodTablePanel.setOutputMarkupId(true);
        this.add(methodTablePanel);

        // attribute form
        this.kindPanel = new KindPanel("kindPanel", xwadl);
        this.kindPanel.setOutputMarkupId(true);
        this.add(this.kindPanel);
    }

    private class XwadlForm extends Form {
        private String resourcePath = "/iaas";

        public XwadlForm(String markupId) {
            super(markupId);
            this.setDefaultModel(new CompoundPropertyModel<Object>(this));

            this.add(new Label("domain", domainModel));
            this.add(new TextField<>("resourcePath", new PropertyModel<>(this, "resourcePath")).setRequired(true));
            AjaxButton button = new AjaxButton("getXwadlBtn") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    try {
                        xwadl = IntercloudWebSession.get().getXmppService().getXwadlDocument(new XmppURI(domainModel.getObject(), resourcePath));
                        target.add(xwadlCodePanel);
                        methodTablePanel.setMethodList(xwadl.getResourceType().getMethodArray());
                        target.add(methodTablePanel);
                    } catch (Exception e) {
                        StringBuilder s = new StringBuilder();
                        s.append("Failed to receive xwadl from xmpp://").append(domainModel.getObject()).append("#").append(resourcePath).append(".");
                        logger.error(s.toString(), e);
                        target.appendJavaScript(s.insert(0, "alert('").append("');").toString());
                        return;
                    }
                    try {
                        kindPanel.setKind(xwadl);
                        target.add(kindPanel);
                    } catch (Exception e) {
                        logger.error("Failed to read kind attributes.", e);
                        logger.error("Failed to read kind attributes.");
                        return;
                    }
                }
            };
            this.add(button);
            this.setDefaultButton(button);
        }
    }
}
