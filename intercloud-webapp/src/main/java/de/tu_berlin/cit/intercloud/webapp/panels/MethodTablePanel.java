package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.XmppMethod;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.DocumentationType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.MethodDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.RequestDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResponseDocument;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodTablePanel extends Panel {
    private List<MethodDocument.Method> methodList = new ArrayList<>();

    public MethodTablePanel(String markupId) {
        super(markupId);

        this.add(new ListView<MethodDocument.Method>("methodList", new LoadableDetachableModel<List<MethodDocument.Method>>() {
            @Override
            protected List<MethodDocument.Method> load() {
                return methodList;
            }
        }) {
            @Override
            protected void populateItem(ListItem<MethodDocument.Method> listItem) {
                MethodDocument.Method method = listItem.getModelObject();
                listItem.add(getMethod("method", method.getType().toString()));
                listItem.add(getDocumentation("documentation", method.getDocumentation()));
                listItem.add(getRequestMediaType("request", method.getRequest()));
                listItem.add(getResponseMediaType("response", method.getResponse()));
            }
        });
    }

    public MethodTablePanel setMethodList(MethodDocument.Method[] methodArray) {
        if (null != methodArray) {
            methodList = Arrays.asList(methodArray);
        } else {
            methodList = new ArrayList<>();
        }
        return this;
    }

    private Link getMethod(String markupId, String type) {
        Link link = new Link(markupId) {
            @Override
            public void onClick() {
                // nothing
            }
        };
        link.setBody(Model.of(type));
        if (XmppMethod.GET.equals(type)) {
            link.add(new AttributeModifier("class", Model.of("btn btn-success")));
        } else if (XmppMethod.DELETE.equals(type)) {
            link.add(new AttributeModifier("class", Model.of("btn btn-danger")));
        } else if (XmppMethod.POST.equals(type)) {
            link.add(new AttributeModifier("class", Model.of("btn btn-info")));
        } else if (XmppMethod.PUT.equals(type)) {
            link.add(new AttributeModifier("class", Model.of("btn btn-warn")));
        }
        return link;
    }

    private Label getDocumentation(String markupId, DocumentationType documentationType) {
        Label label = new Label(markupId);
        if (null != documentationType) {
            String documentation =  documentationType.getStringValue();
            if (null != documentation) {
                label.setDefaultModel(Model.of(documentation));
            }
        }
        return label;
    }

    private Label getRequestMediaType(String markupId, RequestDocument.Request request) {
        Label label = new Label(markupId);
        if (null != request) {
            label.setDefaultModel(Model.of(request.getMediaType()));
        }
        return label;
    }

    private Label getResponseMediaType(String markupId, ResponseDocument.Response response) {
        Label label = new Label(markupId);
        if (null != response) {
            label.setDefaultModel(Model.of(response.getMediaType()));
        }
        return label;
    }
}
