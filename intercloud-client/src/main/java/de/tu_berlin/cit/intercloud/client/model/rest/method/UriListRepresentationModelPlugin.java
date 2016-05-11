package de.tu_berlin.cit.intercloud.client.model.rest.method;

import de.tu_berlin.cit.rwx4j.rest.ResponseDocument;
import de.tu_berlin.cit.rwx4j.xwadl.GrammarsDocument;
import de.tu_berlin.cit.rwx4j.xwadl.RequestDocument;

import java.util.Arrays;
import java.util.List;

public class UriListRepresentationModelPlugin implements IRepresentationModelPlugin<UriListRepresentationModel> {

    @Override
    public String getMediaType() {
        return "text/uri-list";
    }

    @Override
    public UriListRepresentationModel getRequestModel(RequestDocument.Request request, GrammarsDocument.Grammars grammars) {
        return new UriListRepresentationModel();
    }

    @Override
    public String getRepresentationString(UriListRepresentationModel representationModel) {
        String result = null;
        if (null != representationModel) {
            List<String> uriList = representationModel.getUriList();
            if (null != uriList) {
                result = String.join(";", uriList);
            }
        }
        return result;
    }

    @Override
    public UriListRepresentationModel getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars) {
        UriListRepresentationModel result = null;
        if (null != response) {
            result = new UriListRepresentationModel();
            if (null != response.getRepresentation()) {
                String[] links = response.getRepresentation().split(";");
                if (null != links) {
                    result.setUriList(Arrays.asList(links));
                }
            }
        }
        return result;
    }
}
