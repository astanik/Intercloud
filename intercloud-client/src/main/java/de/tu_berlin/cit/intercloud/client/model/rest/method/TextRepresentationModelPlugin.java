package de.tu_berlin.cit.intercloud.client.model.rest.method;

import de.tu_berlin.cit.rwx4j.rest.ResponseDocument;
import de.tu_berlin.cit.rwx4j.xwadl.GrammarsDocument;
import de.tu_berlin.cit.rwx4j.xwadl.RequestDocument;

public class TextRepresentationModelPlugin implements IRepresentationModelPlugin<TextRepresentationModel> {

    @Override
    public String getMediaType() {
        return "text/plain";
    }

    @Override
    public TextRepresentationModel getRequestModel(RequestDocument.Request request, GrammarsDocument.Grammars grammars) {
        return new TextRepresentationModel();
    }

    @Override
    public String getRepresentationString(TextRepresentationModel representationModel) {
        return null != representationModel ? representationModel.getText() : null;
    }

    @Override
    public TextRepresentationModel getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars) {
        return null != response ? new TextRepresentationModel(response.getRepresentation()) : null;
    }
}
