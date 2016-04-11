package de.tu_berlin.cit.intercloud.client.model.rest.method;

import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResponseDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.GrammarsDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.RequestDocument;

public interface IRepresentationModelPlugin<R extends IRepresentationModel> {
    String getMediaType();

    R getRequestModel(RequestDocument.Request request, GrammarsDocument.Grammars grammars);

    String getRepresentationString(R representationModel);

    R getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars);
}
