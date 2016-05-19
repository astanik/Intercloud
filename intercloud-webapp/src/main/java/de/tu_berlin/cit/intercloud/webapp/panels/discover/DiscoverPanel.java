package de.tu_berlin.cit.intercloud.webapp.panels.discover;

import de.tu_berlin.cit.intercloud.webapp.pages.IDiscoverPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * The panel provides the input for {@link IDiscoverPage#discover(String)}
 * of the {@link IDiscoverPage} containing this panl.
 */
public class DiscoverPanel extends Panel {

    public DiscoverPanel(String markupId, IModel<String> jid, IDiscoverPage discoverPage) {
        super(markupId);

        Form form = new Form("discoverForm");
        form.add(new TextField<>("jid", jid).setRequired(true));
        form.add(new Button("discoverBtn") {
            @Override
            public void onSubmit() {
                discoverPage.discover(jid.getObject());
            }
        });
        this.add(form);
    }
}
