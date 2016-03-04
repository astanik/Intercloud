package de.tu_berlin.cit.intercloud.webapp.panels;

import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.CodeBehavior;
import de.tu_berlin.cit.intercloud.client.model.LoggingModel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class LoggingPanel extends Panel {
    public LoggingPanel(String id, IModel<LoggingModel> loggingModel) {
        super(id);

        this.add(newCodePanel("xwadlCode", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return null != loggingModel.getObject() ? loggingModel.getObject().getXwadl() : null;
            }
        }));
        this.add(newCodePanel("restRequestCode", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return null != loggingModel.getObject() ? loggingModel.getObject().getRestRequest() : null;
            }
        }));
        this.add(newCodePanel("restResponseCode", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return null != loggingModel.getObject() ? loggingModel.getObject().getRestResponse() : null;
            }
        }));
    }

    private Code newCodePanel(String markupId, IModel<String> model) {
        Code code = new Code(markupId, model);
        code.setLanguage(CodeBehavior.Language.XML);
        return code;
    }
}
