package de.tu_berlin.cit.intercloud.client.model.occi;

import de.tu_berlin.cit.intercloud.client.model.occi.convert.ClassificationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.RepresentationModelBuilder;
import de.tu_berlin.cit.intercloud.client.model.occi.convert.RepresentationModelConverter;
import de.tu_berlin.cit.intercloud.client.model.rest.method.IRepresentationModelPlugin;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryListDocument;
import de.tu_berlin.cit.rwx4j.rest.ResponseDocument;
import de.tu_berlin.cit.rwx4j.xwadl.GrammarsDocument;
import de.tu_berlin.cit.rwx4j.xwadl.RequestDocument;
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
    public OcciListRepresentationModel getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars) {
        OcciListRepresentationModel result = null;
        if (null != response && null != response.getRepresentation()) {
            try {
                CategoryListDocument categoryListDocument = CategoryListDocument.Factory.parse(response.getRepresentation());
                ClassificationModel classificationModel = ClassificationModelBuilder.build(grammars, false);
                result = RepresentationModelBuilder.build(categoryListDocument, classificationModel);
            } catch (XmlException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return result;
    }
}
