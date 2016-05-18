package de.tu_berlin.cit.intercloud.webapp.panels.discover;

import de.tu_berlin.cit.intercloud.webapp.pages.IDiscoverPage;
import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

public class DiscoveredItemsPanel extends Panel {
    private IModel<List<XmppURI>> discoItems;

    public DiscoveredItemsPanel(String id, IModel<List<XmppURI>> discoItems, IDiscoverPage discoverPage) {
        super(id);
        this.discoItems = discoItems;

        Form form = new Form("itemsForm");
        RadioGroup<XmppURI> radioGroup = new RadioGroup<>("radioGroup", new Model<>());
        radioGroup.add(new ListView<XmppURI>("radioView", discoItems) {
            @Override
            protected void populateItem(ListItem<XmppURI> listItem) {
                listItem.add(new Radio<>("radioItem", listItem.getModel()));
                listItem.add(new Label("radioLabel", listItem.getModelObject().getJID()));
            }
        });
        form.add(radioGroup);

        form.add(new Button("connectBtn") {
            @Override
            public void onSubmit() {
                discoverPage.connect(radioGroup.getModel().getObject());
            }
        });
        this.add(form);
    }

    @Override
    public boolean isVisible() {
        return null != this.discoItems.getObject();
    }
}
