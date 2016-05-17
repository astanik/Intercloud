package de.tu_berlin.cit.intercloud.webapp.panels.browser;

import de.tu_berlin.cit.intercloud.client.model.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.method.MethodModel;
import de.tu_berlin.cit.intercloud.webapp.pages.IBrowserPage;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

public class ActionTablePanel extends Panel {
    IModel<List<ActionModel>> actionList;

    public ActionTablePanel(String id, IModel<List<ActionModel>> actionList, IBrowserPage browserPage) {
        super(id);
        this.actionList = actionList;

        this.add(new ListView<ActionModel>("actionList", actionList) {
            @Override
            protected void populateItem(ListItem<ActionModel> listItem) {
                ActionModel actionModel = listItem.getModelObject();
                listItem.add(newLink("actionLink", actionModel, browserPage));
                listItem.add(newLabel("documentation", actionModel.getDocumentation()));
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

    private AbstractLink newLink(String markupId, ActionModel actionModel, IBrowserPage browserPage) {
        AbstractLink link = new Link(markupId) {
            @Override
            public void onClick() {
                browserPage.selectAction(actionModel);
            }
        }.setBody(Model.of(actionModel.getName()));
        return link;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (null == this.actionList.getObject() || this.actionList.getObject().isEmpty()) {
            this.setVisible(false);
        } else {
            this.setVisible(true);
        }
    }
}
