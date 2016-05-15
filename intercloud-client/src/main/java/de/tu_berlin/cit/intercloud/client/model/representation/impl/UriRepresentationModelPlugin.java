package de.tu_berlin.cit.intercloud.client.model.representation.impl;

import de.tu_berlin.cit.intercloud.client.model.representation.api.IRepresentationModelPlugin;
import de.tu_berlin.cit.rwx4j.rest.ResponseDocument;
import de.tu_berlin.cit.rwx4j.xwadl.GrammarsDocument;
import de.tu_berlin.cit.rwx4j.xwadl.RequestDocument;

public class UriRepresentationModelPlugin implements IRepresentationModelPlugin<UriRepresentationModel> {

    @Override
    public String getMediaType() {
        return "text/uri";
    }

    @Override
    public UriRepresentationModel getRequestModel(RequestDocument.Request request, GrammarsDocument.Grammars grammars) {
        return new UriRepresentationModel();
    }

    @Override
    public String getRepresentationString(UriRepresentationModel representationModel) {
        return null != representationModel ? representationModel.getUri() : null;
    }

    @Override
    public UriRepresentationModel getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars) {
        return null != response ? new UriRepresentationModel(response.getRepresentation()) : null;
    }
}
