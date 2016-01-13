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
import java.util.ArrayList;
import java.util.List;

public class ActionModelConverter {
    private static final Logger logger = LoggerFactory.getLogger(ActionModelConverter.class);

    public static ActionDocument.Action convertToXml(ActionModel actionModel, List<ParameterModel> parameterModelList) throws ParameterFormatException {
        ActionDocument.Action action = ActionDocument.Action.Factory.newInstance();
        action.setName(actionModel.getName());
        if (null != parameterModelList && !parameterModelList.isEmpty()) {
            action.setParameterArray(getParameterArray(parameterModelList));
        }
        return action;
    }

    private static ParameterDocument.Parameter[] getParameterArray(List<ParameterModel> parameterModelList) throws ParameterFormatException {
        List<ParameterDocument.Parameter> resultList = new ArrayList<>();
        for (ParameterModel parameterModel : parameterModelList) {
            if (parameterModel.hasValue()) {
                resultList.add(getParameter(parameterModel));
            }
        }
        return resultList.toArray(new ParameterDocument.Parameter[resultList.size()]);
    }

    private static ParameterDocument.Parameter getParameter(ParameterModel parameterModel) throws ParameterFormatException {
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
        final boolean required = true;
        ParameterModel resultModel = null;
        if (result.isSetBOOLEAN()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.BOOLEAN, required, documentation);
            resultModel.setBoolean(result.getBOOLEAN());
        } else if (result.isSetDOUBLE()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.DOUBLE, required, documentation);
            resultModel.setDouble(result.getDOUBLE());
        } else if (result.isSetINTEGER()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.INTEGER, required, documentation);
            resultModel.setInteger(result.getINTEGER().intValue());
        } else if (result.isSetLINK()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.LINK, required, documentation);
            resultModel.setLink(result.getLINK());
        } else if (result.isSetSTRING()) {
            resultModel = new ParameterModel(parameterName, ParameterModel.Type.STRING, required, documentation);
            resultModel.setString(result.getSTRING());
        } else {
            logger.error("Failed to convert action result to model: value not set. {} ", result);
        }
        return resultModel;
    }
}
