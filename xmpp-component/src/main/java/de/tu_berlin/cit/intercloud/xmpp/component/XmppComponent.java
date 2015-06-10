package de.tu_berlin.cit.intercloud.xmpp.component;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.whack.ExternalComponentManager;

import de.tu_berlin.cit.intercloud.xmpp.core.component.ComponentException;



public class XmppComponent {

	/**
     * Main
     *
     */
	public static void main(String [] args) {
		ExternalComponentManager mgr = new ExternalComponentManager("stanik.cit.tu-berlin.de", 5275);
	      mgr.setSecretKey("exchange", "intercloud");
	      try {
	         mgr.addComponent("exchange", new ExchangeComponent());
	      } catch (ComponentException e) {
	         Logger.getLogger(XmppComponent.class.getName()).log(Level.SEVERE, "XmppComponent", e);
	         System.exit(-1);
	      }
	      //Keep it alive
	      while (true)
	         try {
	            Thread.sleep(10000);
	         } catch (Exception e) {
	            Logger.getLogger(XmppComponent.class.getName()).log(Level.SEVERE, "XmppComponent", e);
	         }
	}

}
