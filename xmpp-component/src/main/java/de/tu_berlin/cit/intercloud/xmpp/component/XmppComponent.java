package de.tu_berlin.cit.intercloud.xmpp.component;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.whack.ExternalComponentManager;

import de.tu_berlin.cit.intercloud.occi.infrastructure.Compute;
import de.tu_berlin.cit.intercloud.xmpp.core.component.ComponentException;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.XmppURI;

public class XmppComponent {

	/**
	 * Main
	 *
	 */
	public static void main(String[] args) {
		ExternalComponentManager mgr = new ExternalComponentManager(
				"cit-mac1.cit.tu-berlin.de", 5275);
		mgr.setSecretKey("exchange", "intercloud");
		try {
			XmppURI uri = new XmppURI("exchange.intercloud.cit.tu-berlin.de", "");
			ResourceContainer container = new ResourceContainer(uri);
			container.addResource(new Compute());
			ExchangeComponent component = new ExchangeComponent(container);
			mgr.addComponent("exchange", component);
		} catch (ComponentException | URISyntaxException e) {
			Logger.getLogger(XmppComponent.class.getName()).log(Level.SEVERE,
					"XmppComponent", e);
			System.exit(-1);
		}
		// Keep it alive
		while (true)
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				Logger.getLogger(XmppComponent.class.getName()).log(
						Level.SEVERE, "XmppComponent", e);
			}
	}

}
