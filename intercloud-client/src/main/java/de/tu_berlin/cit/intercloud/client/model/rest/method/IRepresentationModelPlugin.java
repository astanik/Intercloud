package de.tu_berlin.cit.intercloud.client.model.rest.method;

import de.tu_berlin.cit.rwx4j.rest.ResponseDocument;
import de.tu_berlin.cit.rwx4j.xwadl.GrammarsDocument;
import de.tu_berlin.cit.rwx4j.xwadl.RequestDocument;

public interface IRepresentationModelPlugin<R extends IRepresentationModel> {
    String getMediaType();

    R getRequestModel(RequestDocument.Request request, GrammarsDocument.Grammars grammars);

    String getRepresentationString(R representationModel);

    R getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars);
}
