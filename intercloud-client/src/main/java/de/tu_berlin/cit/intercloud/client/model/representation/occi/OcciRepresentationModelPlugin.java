package de.tu_berlin.cit.intercloud.client.model.representation.occi;

import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.convert.ClassificationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.convert.RepresentationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.convert.RepresentationModelConverter;
import de.tu_berlin.cit.intercloud.client.model.representation.occi.convert.TemplateHelper;
import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.rwx4j.rest.ResponseDocument;
import de.tu_berlin.cit.rwx4j.xwadl.GrammarsDocument;
import de.tu_berlin.cit.rwx4j.xwadl.RequestDocument;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OcciRepresentationModelPlugin implements IRepresentationModelPlugin<OcciRepresentationModel> {
    private static final Logger logger = LoggerFactory.getLogger(OcciRepresentationModelPlugin.class);

    @Override
    public String getMediaType() {
        return "xml/occi";
    }

    @Override
    public OcciRepresentationModel getRequestModel(RequestDocument.Request request, GrammarsDocument.Grammars grammars) {
        ClassificationModel classificationModel = ClassificationModelBuilder.build(grammars, true);
        if (null == classificationModel) {
            throw new MissingClassificationException("Classification is not specified in xwadl.");
        }
        TemplateHelper.addTemplatesToClassificationModel(classificationModel, request);
        OcciRepresentationModel representation = RepresentationModelBuilder.build(classificationModel);
        return representation;
    }

    @Override
    public String getRepresentationString(OcciRepresentationModel representationModel) {
        long start = System.currentTimeMillis();
        String representation = null != representationModel
                ? RepresentationModelConverter.convertToXml(representationModel).toString()
                : null;
        logger.info("RepresentationModel --> XmlBean: {} ms", System.currentTimeMillis() - start);
        return representation;
    }

    @Override
    public OcciRepresentationModel getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars) {
        OcciRepresentationModel result = null;
        if (null != response && null != response.getRepresentation()) {
            try {
                CategoryDocument categoryDocument = CategoryDocument.Factory.parse(response.getRepresentation());
                ClassificationModel classificationModel = ClassificationModelBuilder.build(grammars, false);
                result = RepresentationModelBuilder.build(categoryDocument, classificationModel);
            } catch (XmlException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return result;
    }
}
