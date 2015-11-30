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

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.UpdateListener;

import de.tu_berlin.cit.intercloud.occi.core.Link;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.sla.LengthWindowMetricMixin;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceEvaluatorLink;
import de.tu_berlin.cit.intercloud.occi.sla.TimeWindowMetricMixin;
import de.tu_berlin.cit.intercloud.xmpp.cep.StatementBuilder;
import de.tu_berlin.cit.intercloud.xmpp.cep.mixins.EventLogMixin;

/**
 * This class is the super class that implements basics of an evaluation link.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public abstract class AbstractGuaranteeTerm extends Link {
	
	public class ViolationListener implements UpdateListener {
	    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
	    	for(EventBean event : newEvents) {
	    		EventType type = event.getEventType();
	    		String[] pro = type.getPropertyNames();
	    		if(pro.length >= 1) {
	    			Object o = event.get(pro[0]);
	    			if(o == null)
	    				violated(null);
	    			else
	    				violated(o.toString());
	    		}
	    	}
	    }
	}

	public class FulfillListener implements UpdateListener {
	    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
	    	for(EventBean event : newEvents) {
	    		EventType type = event.getEventType();
	    		String[] pro = type.getPropertyNames();
	    		if(pro.length >= 1) {
	    			Object o = event.get(pro[0]);
	    			if(o == null)
	    				fulfilled(null);
	    			else
	    				fulfilled(o.toString());
	    		}
	    	}
	    }
	}

	private EPStatement violationStatement;
	
	private EPStatement fulfillStatement;

	public AbstractGuaranteeTerm(LinkType representation) {
		super(representation);
	}
	
	protected void buildStatements(ServiceEvaluatorLink evaluator, EventLogMixin eventLog, TimeWindowMetricMixin timeWindow, String slo) {
		violationStatement = StatementBuilder.buildViolationStatement(evaluator, eventLog, timeWindow, slo);
		ViolationListener violationListener = new ViolationListener();
		violationStatement.addListener(violationListener);
		
		fulfillStatement = StatementBuilder.buildFulfillStatement(evaluator, eventLog, timeWindow, slo);
		FulfillListener fulfillListener = new FulfillListener();
		fulfillStatement.addListener(fulfillListener);
	}

	protected void buildStatements(ServiceEvaluatorLink evaluator, EventLogMixin eventLog, LengthWindowMetricMixin lengthWindow, String slo) {
		violationStatement = StatementBuilder.buildViolationStatement(evaluator, eventLog, lengthWindow, slo);
		ViolationListener violationListener = new ViolationListener();
		violationStatement.addListener(violationListener);
		
		fulfillStatement = StatementBuilder.buildFulfillStatement(evaluator, eventLog, lengthWindow, slo);
		FulfillListener fulfillListener = new FulfillListener();
		fulfillStatement.addListener(fulfillListener);
	}

	protected abstract void violated(String value);
	
	protected abstract void fulfilled(String value);

}
