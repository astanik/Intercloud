package de.tu_berlin.cit.intercloud.client.model.occi;

import de.tu_berlin.cit.intercloud.client.exception.MissingClassificationException;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.ClassificationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.RepresentationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.RepresentationModelConverter;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.TemplateHelper;
import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResponseDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.GrammarsDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.RequestDocument;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
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
        long start = System.currentTimeMillis();

        ClassificationDocument.Classification classificationDocument = getClassification(grammars);
        if (null == classificationDocument) {
            throw new MissingClassificationException("Classification is not specified in xwadl.");
        }
        ClassificationModel classificationModel = ClassificationModelBuilder.build(classificationDocument);
        TemplateHelper.addTemplatesToClassificationModel(classificationModel, request);
        OcciRepresentationModel representation = RepresentationModelBuilder.build(classificationModel);

        logger.info("XmlBean --> RepresentationModel: {} ms", System.currentTimeMillis() - start);
        return representation;
    }

    public ClassificationDocument.Classification getClassification(GrammarsDocument.Grammars grammars) {
        ClassificationDocument.Classification result = null;
        if (null != grammars) {
            XmlObject[] classifications = grammars.selectChildren("urn:xmpp:occi-classification", "Classification");
            if (null != classifications && 0 < classifications.length) {
                result = (ClassificationDocument.Classification) classifications[0];
            }
        }
        return result;
    }

    @Override
    public String getRepresentationString(OcciRepresentationModel representationModel) {
        return null != representationModel
                ? RepresentationModelConverter.convertToXml(representationModel).toString()
                : null;
    }

    @Override
    public OcciRepresentationModel getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars) throws XmlException {
        OcciRepresentationModel result = null;
        if (response != null) {
            CategoryDocument categoryDocument = CategoryDocument.Factory.parse(response.getRepresentation());
            ClassificationDocument.Classification classification = getClassification(grammars);
            ClassificationModel classificationModel = ClassificationModelBuilder.build(classification);
            result = RepresentationModelBuilder.build(classificationModel, categoryDocument);
        }
        return result;
    }
}
