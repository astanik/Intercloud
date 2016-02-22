package de.tu_berlin.cit.intercloud.client.model.occi;

import de.tu_berlin.cit.intercloud.client.model.occi.convert.ClassificationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.RepresentationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.RepresentationModelConverter;
import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.occi.core.xml.classification.ClassificationDocument;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryListDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResponseDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.GrammarsDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.RequestDocument;
import org.apache.xmlbeans.XmlException;

import java.util.Arrays;

public class OcciListRepresentationModelPlugin implements IRepresentationModelPlugin<OcciListRepresentationModel> {

    private OcciRepresentationModelPlugin occiRepresentationModelPlugin = new OcciRepresentationModelPlugin();

    @Override
    public String getMediaType() {
        return "xml/occi-list";
    }

    @Override
    public OcciListRepresentationModel getRequestModel(RequestDocument.Request request, GrammarsDocument.Grammars grammars) {
        return new OcciListRepresentationModel(Arrays.asList(occiRepresentationModelPlugin.getRequestModel(request, grammars)));
    }

    @Override
    public String getRepresentationString(OcciListRepresentationModel representationModel) {
        return null != representationModel
                ? RepresentationModelConverter.convertToXml(representationModel).toString()
                : null;
    }

    @Override
    public OcciListRepresentationModel getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars) throws XmlException {
        OcciListRepresentationModel result = null;
        if (null != response) {
            CategoryListDocument categoryListDocument = CategoryListDocument.Factory.parse(response.getRepresentation());
            ClassificationDocument.Classification classification = occiRepresentationModelPlugin.getClassification(grammars);
            ClassificationModel classificationModel = ClassificationModelBuilder.build(classification);
            result = RepresentationModelBuilder.build(classificationModel, categoryListDocument);
        }
        return result;
    }
}
