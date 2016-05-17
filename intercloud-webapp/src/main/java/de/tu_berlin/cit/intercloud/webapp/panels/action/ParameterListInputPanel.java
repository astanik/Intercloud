package de.tu_berlin.cit.intercloud.webapp.panels.action;

import de.tu_berlin.cit.intercloud.client.model.action.ParameterModel;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.AttributeModel;
import de.tu_berlin.cit.intercloud.webapp.panels.method.request.AttributeInputPanel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class ParameterListInputPanel extends Panel {

    public ParameterListInputPanel(String markupId, IModel<List<ParameterModel>> parameterModels) {
        super(markupId);

        this.add(new ListView<ParameterModel>("parameterList", parameterModels) {
            @Override
            protected void populateItem(ListItem<ParameterModel> listItem) {
                listItem.add(new ParameterInputPanel("parameterInput", listItem.getModel()));
            }
        });
    }
}
