package de.tu_berlin.cit.intercloud.webapp.panels.browser;

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

/**
 * This Panel displays the {@link MethodModel}s provided by a resource.
 * In order to select an {@link MethodModel}, it invokes the
 * {@link IBrowserPage#selectMethod(MethodModel)} of the {@link IBrowserPage} containing this panel.
 */
public class MethodTablePanel extends Panel {
    IModel<List<MethodModel>> methodList;

    public MethodTablePanel(String id, IModel<List<MethodModel>> methodList, IBrowserPage browserPage) {
        super(id);
        this.methodList = methodList;

        this.add(new ListView<MethodModel>("methodList", methodList) {
            @Override
            protected void populateItem(ListItem<MethodModel> listItem) {
                MethodModel methodModel = listItem.getModelObject();
                listItem.add(newLink("methodLink", methodModel, browserPage));
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

    private AbstractLink newLink(String markupId, MethodModel methodModel, IBrowserPage browserPage) {
        AbstractLink link = new Link(markupId) {
            @Override
            public void onClick() {
                browserPage.selectMethod(methodModel);
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

    @Override
    public boolean isVisible() {
        return null != this.methodList.getObject() && !this.methodList.getObject().isEmpty();
    }
}
