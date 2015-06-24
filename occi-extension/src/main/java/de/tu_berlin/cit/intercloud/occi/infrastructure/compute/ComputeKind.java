package de.tu_berlin.cit.intercloud.occi.infrastructure.compute;

import java.net.URI;
import java.net.URISyntaxException;

import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;

public class ComputeKind extends Kind {

	public ComputeKind() throws URISyntaxException {
		super(new URI("http://example.org"), "", "");
		// TODO
	}
}
