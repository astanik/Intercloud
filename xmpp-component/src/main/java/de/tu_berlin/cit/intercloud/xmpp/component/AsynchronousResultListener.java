package de.tu_berlin.cit.intercloud.xmpp.component;

import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;

public interface AsynchronousResultListener {

	public void processResult(ResourceDocument doc);
	
}
