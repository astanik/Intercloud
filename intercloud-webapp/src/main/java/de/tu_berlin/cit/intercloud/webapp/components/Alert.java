package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class Alert extends de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert {
    public Alert(String id, IModel<String> message) {
        super(id, message);
    }

    public Alert(String id, IModel<String> message, IModel<String> header) {
        super(id, message, header);
    }

    @Override
    public boolean isVisible() {
        return null != this.getModel().getObject();
    }

    public Alert withError(Throwable t) {
        this.type(Type.Danger);
        this.withHeader(Model.of(ExceptionUtils.getMessage(t)));
        this.withMessage(Model.of(ExceptionUtils.getStackTrace(t)));
        return this;
    }

    public Alert withInfo(IModel<String> message, IModel<String> header) {
        this.type(Type.Info);
        this.withHeader(header);
        this.withMessage(message);
        return this;
    }

    public Alert withWarning(IModel<String> message, IModel<String> header) {
        this.type(Type.Warning);
        this.withHeader(header);
        this.withMessage(message);
        return this;
    }

    public Alert noMessage() {
        this.withMessage(Model.of());
        return this;
    }
}
