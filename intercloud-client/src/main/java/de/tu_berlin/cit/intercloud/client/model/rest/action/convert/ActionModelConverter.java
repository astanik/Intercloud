package de.tu_berlin.cit.intercloud.client.model.rest.action.convert;

import de.tu_berlin.cit.intercloud.client.exception.ParameterFormatException;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ActionModel;
import de.tu_berlin.cit.intercloud.client.model.rest.action.ParameterModel;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ActionDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ParameterDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResultDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class ActionModelConverter {
    private static final Logger logger = LoggerFactory.getLogger(ActionModelConverter.class);

    public static ActionDocument.Action convertToXml(ActionModel actionModel) throws ParameterFormatException {
        ActionDocument.Action action = ActionDocument.Action.Factory.newInstance();
        action.setName(actionModel.getName());
        // action.setResult( TODO: need to set result?
        action.setParameterArray(getParameterArray(actionModel));
        return action;
    }

    private static ParameterDocument.Parameter[] getParameterArray(ActionModel actionModel) throws ParameterFormatException {
        if (null == actionModel.getParameterList() || actionModel.getParameterList().isEmpty()) {
            return null;
        }
        ParameterDocument.Parameter[] resultArray = new ParameterDocument.Parameter[actionModel.getParameterList().size()];
        int i = 0;
        for (ParameterModel parameterModel : actionModel.getParameterList()) {
            resultArray[i++] = getParameter(parameterModel);
        }
        return resultArray;
    }

    private static ParameterDocument.Parameter getParameter(ParameterModel parameterModel) throws ParameterFormatException {
        // TODO: parameter alway required?
        ParameterDocument.Parameter result = ParameterDocument.Parameter.Factory.newInstance();
        result.setName(parameterModel.getName());
        switch (parameterModel.getType()) {
            case STRING:
                result.setSTRING(parameterModel.getString());
                break;
            case INTEGER:
                result.setINTEGER(BigInteger.valueOf(parameterModel.getInteger()));
                break;
            case DOUBLE:
                result.setDOUBLE(parameterModel.getDouble());
                break;
            case BOOLEAN:
                result.setBOOLEAN(parameterModel.getBoolean());
                break;
            case LINK:
                result.setLINK(parameterModel.getLink());
                break;
            default:
                throw new ParameterFormatException("Cannot set Parameter: type is not supported " + parameterModel);
        }
        return result;
    }

    public static ParameterModel convertToModel(ResultDocument.Result result, String documentation) {
        final String parameterName = "result";
        ParameterModel resultModel = null;
        if (result.isSetBOOLEAN()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.BOOLEAN, documentation);
            resultModel.setBoolean(result.getBOOLEAN());
        } else if (result.isSetDOUBLE()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.DOUBLE, documentation);
            resultModel.setDouble(result.getDOUBLE());
        } else if (result.isSetINTEGER()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.INTEGER, documentation);
            resultModel.setInteger(result.getINTEGER().intValue());
        } else if (result.isSetLINK()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.LINK, documentation);
            resultModel.setLink(result.getLINK());
        } else if (result.isSetSTRING()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.STRING, documentation);
            resultModel.setString(result.getSTRING());
        } else {
            logger.error("Failed to convert action result to model: value not set. {} ", result);
        }
        return resultModel;
    }
}
