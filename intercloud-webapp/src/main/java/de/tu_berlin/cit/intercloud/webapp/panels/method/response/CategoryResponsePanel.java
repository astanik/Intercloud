package de.tu_berlin.cit.intercloud.webapp.panels.method.response;

import de.tu_berlin.cit.intercloud.client.model.representation.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.CategoryModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import java.util.ArrayList;

public abstract class CategoryResponsePanel extends Panel {
    public CategoryResponsePanel(String id, IModel<? extends CategoryModel> categoryModel) {
        super(id);

        CategoryModel category = categoryModel.getObject();
        this.add(new Label("type", getType()));
        this.add(new Label("term", category.getTerm()));
        this.add(new Label("schema", category.getSchema()));
        this.add(new WebMarkupContainer("titleRow") {
            @Override
            public boolean isVisible() {
                return null != category.getTitle();
            }
        }.add(new Label("title", category.getTitle())));

        this.add(new ListView<AttributeModel>("attributeContainer", new ListModel<>(new ArrayList<>(category.getAttributes()))) {
            @Override
            protected void populateItem(ListItem<AttributeModel> listItem) {
                AttributeModel attribute = listItem.getModelObject();
                listItem.add(new Label("name", attribute.getName()));
                listItem.add(new Label("value", attribute.getValue().toString()));
                listItem.add(new Label("description", attribute.getDescription()));
            }
        });
    }

    protected abstract String getType();
}
