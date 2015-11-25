package de.tu_berlin.cit.intercloud.webapp.pages;

import de.tu_berlin.cit.intercloud.webapp.ComponentUtils;
import de.tu_berlin.cit.intercloud.webapp.IntercloudWebSession;
import de.tu_berlin.cit.intercloud.webapp.template.UserTemplate;
import de.tu_berlin.cit.intercloud.webapp.xmpp.XmppService;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DiscoverItemsPage extends UserTemplate {
    private static final Logger logger = LoggerFactory.getLogger(DiscoverItemsPage.class);

    private final WebMarkupContainer itemsContainer;
    private final List<String> discoItems = new ArrayList<>();

    public DiscoverItemsPage() {
        super();

        this.add(new DiscoverForm("discoverForm"));

        itemsContainer = new WebMarkupContainer("itemsContainer");
        itemsContainer.setOutputMarkupId(true);
        ComponentUtils.displayNone(itemsContainer);
        itemsContainer.add(new ItemsForm("itemsForm"));
        this.add(itemsContainer);
    }

    private class DiscoverForm extends Form {
        private String domain = IntercloudWebSession.get().getUser().getUri().getDomain();

        public DiscoverForm(String markupId) {
            super(markupId);

            this.add(new TextField<>("domain", new PropertyModel<>(this, "domain")).setRequired(true));
            this.add(new AjaxButton("discoverBtn") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    try {
                        AbstractXMPPConnection connection = IntercloudWebSession.get().getConnection();
                        XmppURI uri = new XmppURI(domain, "");
                        discoItems.clear();
                        discoItems.addAll(XmppService.getInstance().discoverXmppRestfulItems(connection, uri));
                        if (!discoItems.isEmpty()) {
                            // display items form
                            target.add(ComponentUtils.displayBlock(itemsContainer));
                        } else {
                            // hide items form and display alert
                            target.appendJavaScript("alert('Cannot find suitable entities for domain " + domain + "'.);");
                            target.add(ComponentUtils.displayNone(itemsContainer));
                        }
                    } catch (Exception e) {
                        // hide items form and display alert
                        target.appendJavaScript("alert('Failed to discover items for domain " + domain + ".');");
                        target.add(ComponentUtils.displayNone(itemsContainer));
                        logger.error("Failed to discover items for domain {}.", domain, e);
                    }
                }
            });
        }
    }

    private class ItemsForm extends Form {
        private final RadioGroup radioGroup;

        public ItemsForm(String markupId) {
            super(markupId);

            radioGroup = new RadioGroup<String>("radioGroup", new Model<>());
            radioGroup.add(new ListView<String>("radioView", discoItems) {
                @Override
                protected void populateItem(ListItem<String> listItem) {
                    listItem.add(new Radio<>("radioItem", listItem.getModel()));
                    listItem.add(new Label("radioLabel", listItem.getModelObject()));
                }
            });
            this.add(radioGroup);

            this.add(new AjaxButton("connectBtn") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    String domain = radioGroup.getDefaultModelObjectAsString();
                    if (null != domain && !domain.trim().isEmpty()) {
                        setResponsePage(new GetXwadlPage(Model.of(domain)));
                    } else {
                        target.appendJavaScript("alert('Please select a value from the radio group!');");
                    }
                }
            });
        }
    }
}
