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

/**
 * Displays a {@link CategoryModel} sub-type in the context of a response,
 * thus it does NOT provide user input.
 */
public abstract class CategoryResponsePanel extends Panel {
    public CategoryResponsePanel(String id, IModel<? extends CategoryModel> categoryModel) {
        super(id);

        CategoryModel category = categoryModel.getObject();
        // display OCCI identification
        this.add(new Label("type", getType()));
        this.add(new Label("term", category.getTerm()));
        this.add(new Label("schema", category.getSchema()));
        // only display title if present
        this.add(new WebMarkupContainer("titleRow") {
            @Override
            public boolean isVisible() {
                return null != category.getTitle();
            }
        }.add(new Label("title", category.getTitle())));

        // display attributes
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

    /**
     * @return The Category sub-type: Kind, Mixin or Link.
     */
    protected abstract String getType();
}
