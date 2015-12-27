package de.tu_berlin.cit.intercloud.client.model.rest.action.convert;

import de.tu_berlin.cit.intercloud.client.model.rest.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ParameterModel;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ActionDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.DocumentationType;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ParameterDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResultDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ActionModelBuilder {
    private final static Logger logger = LoggerFactory.getLogger(ActionModelBuilder.class);

    public static List<ActionModel> buildActionModels(ResourceTypeDocument resourceTypeDocument) {
        List<ActionModel> actionModels = new ArrayList<>();
        ResourceTypeDocument.ResourceType resourceType = resourceTypeDocument.getResourceType();
        if (null != resourceType) {
            ActionDocument.Action[] actionArray = resourceType.getActionArray();
            if (null != actionArray && 0 < actionArray.length) {
                for (ActionDocument.Action action : actionArray) {
                    actionModels.add(buildActionModel(action));
                }
            }
        }
        return actionModels;
    }

    public static ActionModel buildActionModel(ActionDocument.Action action) {
        List<ParameterModel> parameterModels = buildParameterModels(action.getParameterArray());
        ResultDocument.Result result = action.getResult();
        ParameterModel resultModel = null;
        if (null != result) {
            resultModel = new ParameterModel("result", result.getType().toString(), getDocumentation(result.getDocumentation()));
        }
        return new ActionModel(action.getName(), getDocumentation(action.getDocumentation()), parameterModels, resultModel);
    }

    private static String getDocumentation(DocumentationType documentationType) {
        return null != documentationType ? documentationType.getStringValue() : null;
    }

    private static List<ParameterModel> buildParameterModels(ParameterDocument.Parameter[] parameterArray) {
        List<ParameterModel> parameterModels = new ArrayList<>();
        if (null != parameterArray && 0 < parameterArray.length) {
            for (ParameterDocument.Parameter parameter : parameterArray) {
                ParameterModel parameterModel = new ParameterModel(parameter.getName(),
                        parameter.getType().toString(),
                        getDocumentation(parameter.getDocumentation()));
                addParameterDefaultValue(parameterModel, parameter);
                parameterModels.add(parameterModel);
            }
        }
        return parameterModels;
    }

    private static ParameterModel addParameterDefaultValue(ParameterModel parameterModel, ParameterDocument.Parameter parameter) {
        String defaultValue = parameter.getDefault();
        if (null != defaultValue && !defaultValue.trim().isEmpty()) {
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
                        logger.warn("Could not set default parameter, unsupported type");
                        break;
                }
            } catch (Exception e) {
                logger.error("Could not set default parameter.", e);
            }
        }
        return parameterModel;
    }
}
