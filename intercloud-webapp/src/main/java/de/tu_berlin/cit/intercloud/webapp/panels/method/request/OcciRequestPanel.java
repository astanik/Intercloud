package de.tu_berlin.cit.intercloud.webapp.panels.method.request;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.KindModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.LinkModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.OcciRepresentationModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

public class OcciRequestPanel extends Panel {
    private final LinkListRequestPanel linkListPanel;

    public OcciRequestPanel(String id, IModel<OcciRepresentationModel> representationModel) {
        super(id);

        OcciRepresentationModel representation = representationModel.getObject();
        // KIND
        KindModel kindModel = representation.getKind();
        if (null != kindModel) {
            this.add(new KindRequestPanel("kindPanel", new Model<>(kindModel)));
        } else {
            this.add(new EmptyPanel("kindPanel"));
        }

        // MIXINs
        this.add(new MixinListRequestPanel("mixinListPanel", new ListModel<>(representation.getMixins())));

        // LINKs
        this.linkListPanel = new LinkListRequestPanel("linkListPanel", new ListModel<>(representation.getLinks()));
        this.linkListPanel.setOutputMarkupId(true);
        this.add(this.linkListPanel);


        Form linkForm = new Form("linkForm");
        this.add(linkForm);
        DropDownChoice<LinkModel> linkChoice = new DropDownChoice<>("linkSelect", new Model<>(),
                representation.getLinkDefinitions(), new ChoiceRenderer<>("id"));
        linkForm.add(linkChoice);
        linkForm.add(new AjaxButton("addLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                representation.addToLinkList(linkChoice.getModelObject());
                target.add(OcciRequestPanel.this.linkListPanel);
            }
        });
        linkForm.setVisible(!representation.getLinkDefinitions().isEmpty());
    }
}
