package de.tu_berlin.cit.intercloud.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.CodeBehavior;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.intercloud.xmpp.client.XmppRestClient;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetXwadlPage extends UserTemplate {
    private final Logger logger = LoggerFactory.getLogger(GetXwadlPage.class);

    public static String PARAM_DOMAIN = "domain";

    private final String domain;
    private final Code xwadlCodePanel;
    private String xwadlStr = "";

    public GetXwadlPage(final PageParameters parameters) {
        super();
        this.domain = parameters.get(PARAM_DOMAIN).toString("missing domain");

        this.add(new XwadlForm("xwadlForm"));

        this.xwadlCodePanel = new Code("xwadlCode", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return xwadlStr;
            }
        });
        this.xwadlCodePanel.setLanguage(CodeBehavior.Language.XML);
        this.xwadlCodePanel.setOutputMarkupId(true);
        this.add(this.xwadlCodePanel);
    }

    private class XwadlForm extends Form {
        private String path = "/iaas";

        public XwadlForm(String markupId) {
            super(markupId);
            this.setDefaultModel(new CompoundPropertyModel<Object>(this));

            this.add(new Label("domain", Model.of(domain)));
            this.add(new TextField<>("path", new PropertyModel<>(this, "path")).setRequired(true));
            this.add(new AjaxButton("getXwadlBtn") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    try {
                        AbstractXMPPConnection connection = getIntercloudWebSession().getConnection();
                        XmppRestClient restClient = XmppRestClient.XmppRestClientBuilder.build(connection, new XmppURI(domain, path));
                        xwadlStr = restClient.getResourceTypeDocument().toString();
                        target.add(xwadlCodePanel);
                    } catch (Exception e) {
                        StringBuilder s = new StringBuilder();
                        s.append("Failed to receive xwadl from xmpp://").append(domain).append("#").append(path).append(".");
                        logger.error(s.toString(), e);
                        target.appendJavaScript(s.insert(0, "alert('").append("');").toString());
                    }
                }
            });
        }
    }
}
