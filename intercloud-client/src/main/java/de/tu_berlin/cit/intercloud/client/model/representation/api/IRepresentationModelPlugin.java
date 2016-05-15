package de.tu_berlin.cit.intercloud.client.model.representation.api;

import de.tu_berlin.cit.rwx4j.rest.ResponseDocument;
import de.tu_berlin.cit.rwx4j.rest.RestDocument;
import de.tu_berlin.cit.rwx4j.xwadl.GrammarsDocument;
import de.tu_berlin.cit.rwx4j.xwadl.RequestDocument;
import de.tu_berlin.cit.rwx4j.xwadl.XwadlDocument;

/**
 * This interface is associated with a certain Representation Model and thus a certain media type.
 * It provides an extension mechanism associate an implementation of {@link IRepresentationModel} with a media type.
 * It provides methods to convert the Description of a Representation and the Representation itself into
 * the corresponding Model object.
 * @param <R> The Representation Model which is associated with the implementation of this interface.
 */
public interface IRepresentationModelPlugin<R extends IRepresentationModel> {
    /**
     * @return The media type that is associated with the Representation Model defined by the
     * implementation of this interface.
     */
    String getMediaType();

    /**
     * Creates an instance of an {@link IRepresentationModel} associated with the implementation
     * based on the description of a resource provided by an {@link XwadlDocument}.
     * @param request The request may be providing Templates.
     * @param grammars The grammars may define the structure of the Representation Model.
     * @return
     */
    R getRequestModel(RequestDocument.Request request, GrammarsDocument.Grammars grammars);

    /**
     * Converts an {@link IRepresentationModel} associated with the implementation into a
     * string representation used by a {@link RestDocument}
     * @param representationModel
     * @return
     */
    String getRepresentationString(R representationModel);

    /**
     * Creates an instance of an {@link IRepresentationModel} associated with the implementation
     * based on the representation provided by an {@link RestDocument}.
     * @param response The response containing the representation.
     * @param grammars The grammars section serves to verify the representation's structure
     *                 and may provide documentations.
     * @return
     */
    R getResponseModel(ResponseDocument.Response response, GrammarsDocument.Grammars grammars);
}
