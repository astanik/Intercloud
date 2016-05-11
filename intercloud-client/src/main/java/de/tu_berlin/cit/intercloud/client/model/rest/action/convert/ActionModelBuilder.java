package de.tu_berlin.cit.intercloud.client.model.rest.action.convert;

import de.tu_berlin.cit.intercloud.client.model.rest.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ParameterModel;
import de.tu_berlin.cit.rwx4j.xwadl.ActionDocument;
import de.tu_berlin.cit.rwx4j.xwadl.DocumentationType;
import de.tu_berlin.cit.rwx4j.xwadl.ParameterDocument;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ActionModelBuilder {
    private final static Logger logger = LoggerFactory.getLogger(ActionModelBuilder.class);

    public static List<ActionModel> buildActionModels(XwadlDocument xwadlDocument) {
        List<ActionModel> actionModels = new ArrayList<>();
        XwadlDocument.Xwadl xwadl = xwadlDocument.getXwadl();
        if (null != xwadl) {
            ActionDocument.Action[] actionArray = xwadl.getActionArray();
            if (null != actionArray && 0 < actionArray.length) {
                for (ActionDocument.Action action : actionArray) {
                    actionModels.add(new ActionModel(action.getName(),
                            getDocumentation(action.getDocumentation()),
                            action.isSetResult() ? getDocumentation(action.getResult().getDocumentation()) : null,
                            action));
                }
            }
        }
        return actionModels;
    }

    private static String getDocumentation(DocumentationType documentationType) {
        return null != documentationType ? documentationType.getStringValue() : null;
    }

    public static List<ParameterModel> buildParameterModelList(ActionDocument.Action action) {
        return buildParameterModelList(action.getParameterArray());
    }

    private static List<ParameterModel> buildParameterModelList(ParameterDocument.Parameter[] parameterArray) {
        List<ParameterModel> parameterModels = new ArrayList<>();
        if (null != parameterArray && 0 < parameterArray.length) {
            for (ParameterDocument.Parameter parameter : parameterArray) {
                ParameterModel parameterModel;
                String defaultValue = parameter.getDefault();
                if (null != defaultValue && !defaultValue.trim().isEmpty()) {
                    parameterModel = new ParameterModel(parameter.getName(),
                            parameter.getType().toString(),
                            false,
                            getDocumentation(parameter.getDocumentation()));
                    addParameterDefaultValue(parameterModel, defaultValue);
                } else {
                    parameterModel = new ParameterModel(parameter.getName(),
                            parameter.getType().toString(),
                            true,
                            getDocumentation(parameter.getDocumentation()));
                }
                parameterModels.add(parameterModel);
            }
        }
        return parameterModels;
    }

    private static ParameterModel addParameterDefaultValue(ParameterModel parameterModel, String defaultValue) {
        try {
            switch (parameterModel.getType()) {
                case STRING:
                    parameterModel.setString(defaultValue);
                    break;
                case INTEGER:
                    parameterModel.setInteger(Integer.parseInt(defaultValue));
                    break;
                case DOUBLE:
                    parameterModel.setDouble(Double.parseDouble(defaultValue));
                    break;
                case BOOLEAN:
                    parameterModel.setBoolean(Boolean.parseBoolean(defaultValue));
                    break;
                case LINK:
                    parameterModel.setLink(defaultValue);
                    break;
                default:
                    logger.warn("Could not set default parameter, unsupported type. {}", parameterModel);
                    break;
            }
        } catch (Exception e) {
            logger.error("Could not set default parameter. {}", parameterModel, e);
        }
        return parameterModel;
    }
}
