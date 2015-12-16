/**
 * Copyright 2010-2015 Complex and Distributed IT Systems, TU Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tu_berlin.cit.intercloud.xmpp.component;

import java.util.concurrent.Exchanger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.Message;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ.Type;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ResourceContainerSocket {

	private final ResourceContainerSocketManager socketManager;
	
	private final String jid;
	
	protected final Exchanger<ResourceTypeDocument> xwadlExchanger = new Exchanger<ResourceTypeDocument>();
	
	protected final Exchanger<ResourceDocument> restXmlExchanger = new Exchanger<ResourceDocument>();

	/**
	 * This constructor should only be used by the ResourceContainerSocketManager.
	 * 
	 * @param socketManager The ResourceContainerSocketManager instance.
	 */
	protected ResourceContainerSocket(ResourceContainerSocketManager socketManager, String jid) {
		this.socketManager = socketManager;
		this.jid = jid;
	}
	
	public String getJid() {
		return jid;
	}

	public void sendMessage(String message) {
		Message m = new Message();
		m.setTo(jid);
		m.setBody(message);
		this.socketManager.sendMessage(m);
	}
	
	public ResourceTypeDocument requestXWADL(String path) throws InterruptedException {
		IQ iq = new IQ(Type.get);
		iq.setTo(jid);

		// create request
		ResourceTypeDocument request = ResourceTypeDocument.Factory.newInstance();
		request.addNewResourceType().setPath(path);
		try {
			Document doc;
			doc = DocumentHelper.parseText(request.toString());
			// set request
			iq.setChildElement(doc.getRootElement());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		this.socketManager.sendIQ(iq, this);
		
		// wait for response
		ResourceTypeDocument response;
		response = this.xwadlExchanger.exchange(request);
		return response;
	}
	
	public ResourceDocument invokeRestXML(ResourceDocument request) throws InterruptedException {
		IQ iq = new IQ(Type.set);
		iq.setTo(jid);

		// create request
		try {
			Document doc;
			doc = DocumentHelper.parseText(request.toString());
			// set request
			iq.setChildElement(doc.getRootElement());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		this.socketManager.sendIQ(iq, this);
		
		// wait for response
		ResourceDocument response;
		response = this.restXmlExchanger.exchange(request);
		return response;
		
	}

	public void invokeAsyncRestXML(ResourceDocument request, AsynchronousResultListener listener) {
		IQ iq = new IQ(Type.set);
		iq.setTo(jid);

		// create request
		try {
			Document doc;
			doc = DocumentHelper.parseText(request.toString());
			// set request
			iq.setChildElement(doc.getRootElement());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.socketManager.sendIQ(iq, this);
		
		// process the result in a separate thread
		new Thread( new Runnable() {
		    @Override
		    public void run() {
				// wait for response
				ResourceDocument response;
				try {
					response = restXmlExchanger.exchange(request);
					listener.processResult(response);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}).start();
	}

}
