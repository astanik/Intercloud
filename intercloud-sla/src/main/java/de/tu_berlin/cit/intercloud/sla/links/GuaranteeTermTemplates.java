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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.RepresentationBuilder;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceEvaluatorLink;
import de.tu_berlin.cit.intercloud.occi.sla.TimeWindowMetricMixin;
import de.tu_berlin.cit.intercloud.xmpp.cep.events.CpuUtilizationEvent;
import de.tu_berlin.cit.intercloud.xmpp.cep.mixins.EventLogMixin;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class GuaranteeTermTemplates extends OcciXml {

	@Override
	public List<Representation> getTemplates() {
		// create empty template list
		List<Representation> list = new ArrayList<Representation>();

		// create Availability guarantee template
		
		// create CPU utilization guarantee template
/*
		ServiceEvaluatorLink evaluator = new ServiceEvaluatorLink();
		evaluator.setObject(sensorURI);
		evaluator.setSubject(vmURI);
		TimeWindowMetricMixin timeWindow = new TimeWindowMetricMixin();
		timeWindow.durationUnit = TimeWindowMetricMixin.TimeUnit.seconds;
		timeWindow.windowDuration = 5;
		EventLogMixin eventLog = new EventLogMixin();
		eventLog.eventID = CpuUtilizationEvent.CpuUtilizationStream;
*/
/*
		try {
					// create document
					CategoryDocument doc;
					doc = RepresentationBuilder.buildRepresentation(kind);
					// add template
					list.add(new OcciXml(doc));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
*/

		// return templates
		return list;
	}


}
