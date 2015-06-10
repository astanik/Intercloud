package de.tu_berlin.cit.intercloud.xmpp.client;

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


public class XmppClient {

	/**
     * Main
     *
     */
	public static void main(String [] args) {
		// Create a connection configuration
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				  .setUsernameAndPassword("alex", "alex")
				  .setServiceName("jabber.org")
				  .setHost("stanik.cit.tu-berlin.de")
				  .setPort(5222)
				  .build();

		// Create a connection to the jabber.org server.
		AbstractXMPPConnection connection = new XMPPTCPConnection(config);
		try {
			// connect
			connection.connect();
			// login
			connection.login();
			
			//connection.addPacketListener(new MyPacketListener(),new PacketTypeFilter(IQ.class));

			 

			 

	//		class MyPacketListener implements PacketListener{
	//		    public void processPacket(Packet packet){
	//		     System.out.println("Recv : " + packet.toXML());
	//		    }
			    
			ExtensionElement extension = new MethodExtension("GET");
			
			IQ iq = RestIQ.createRestPacket("alex@stanik.", "exchange.cit.tu-berlin.de", Type.set, extension);  
				
			connection.sendStanza(iq);
			
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}

		
	}

}
