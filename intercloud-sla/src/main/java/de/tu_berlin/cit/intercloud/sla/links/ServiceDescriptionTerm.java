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

package de.tu_berlin.cit.intercloud.sla.links;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import de.tu_berlin.cit.intercloud.occi.core.Link;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceReferenceLink;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceReferenceLink.State;
import de.tu_berlin.cit.rwx4j.component.ResourceContainerSocket;
import de.tu_berlin.cit.rwx4j.component.ResourceContainerSocketManager;
import de.tu_berlin.cit.rwx4j.XmppURI;
import de.tu_berlin.cit.rwx4j.annotations.PathID;
import de.tu_berlin.cit.rwx4j.annotations.XmppMethod;
import de.tu_berlin.cit.rwx4j.rest.RestDocument;

/**
 * This link points to a service. Furthermore, it not only links to and describes
 * the service, but it also evaluates the target in an assessment interval based 
 * on the attributes that describes the service within this link.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Classification(links = { ServiceReferenceLink.class })
public class ServiceDescriptionTerm extends Link {

	private ServiceReferenceLink serviceReference;
	
	private Timer timer = null;

	private class AssessmentTimerTask extends TimerTask {
		
		private final ResourceContainerSocket socket;
		
		protected AssessmentTimerTask() throws URISyntaxException {
			ResourceContainerSocketManager mgt = ResourceContainerSocketManager.getInstance();
			XmppURI uri = new XmppURI(serviceReference.getTarget());
			this.socket = mgt.createSocket(uri.getJID());
		}
		
	    @Override
	    public void run() {
	    	// retrieve representation
	    	// TODO
	    	RestDocument request = null;
	    	RestDocument response = null;
	    	try {
				response = this.socket.invokeRestXML(request);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	// TODO
	    	CategoryDocument representation = null;
	    	// evaluate representation
	    	boolean isFulfilled = false;
	    	isFulfilled = evaluate(representation);
	    	// set state
	    	if(isFulfilled) {
	    		serviceReference.state = State.fulfilled;
	    		serviceReference.message = "The service is provided as agreed on";
	    	} else {
	    		serviceReference.state = State.violated;
	    		serviceReference.message = "The service is not provided as agreed on";
	    	}
	    }

		private boolean evaluate(CategoryDocument representation) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	public ServiceDescriptionTerm(ServiceReferenceLink serviceReference) {
		this.serviceReference = serviceReference;
		// set defaults
		this.serviceReference.state = State.undefined;
		this.serviceReference.message = "The Service Description Term has not been evaluated";
		// check assessment
		if(this.serviceReference.assessmentInterval != null) {
			TimerTask timerTask;
			try {
				timerTask = new AssessmentTimerTask();
		        //running timer task as daemon thread
		        this.timer = new Timer(true);
		        // convert assessment interval from seconds to millisecond
		        long assessmentInterval = this.serviceReference.assessmentInterval * 1000;
		        timer.scheduleAtFixedRate(timerTask, 0, assessmentInterval);
		        logger.info("TimerTask started");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@XmppMethod(XmppMethod.DELETE)
	@Override
	public void deleteResource() {
		if(this.timer != null) {
			this.timer.cancel();
        	logger.info("TimerTask cancelled");
		}
		super.deleteResource();
	}
	
}
