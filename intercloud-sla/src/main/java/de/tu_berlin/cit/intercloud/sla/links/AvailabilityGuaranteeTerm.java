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

import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.RepresentationBuilder;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceEvaluatorLink;
import de.tu_berlin.cit.intercloud.occi.sla.TimeWindowMetricMixin;
import de.tu_berlin.cit.intercloud.sla.mixins.AvailabilityMixin;
import de.tu_berlin.cit.intercloud.xmpp.cep.mixins.EventLogMixin;
import de.tu_berlin.cit.rwx4j.annotations.PathID;

/**
 * This class implements an evaluation link that measures the availability of
 * service.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Classification(mixins = {EventLogMixin.class, TimeWindowMetricMixin.class, AvailabilityMixin.class}, 
				links  = {ServiceEvaluatorLink.class})
public class AvailabilityGuaranteeTerm extends AbstractGuaranteeTerm {

	private ServiceEvaluatorLink serviceEvaluatorLink = new ServiceEvaluatorLink();
	
	private EventLogMixin eventLogMixin = new EventLogMixin();
	
	private TimeWindowMetricMixin timeWindowMetricMixin = new TimeWindowMetricMixin();
	
	private AvailabilityMixin availabilityMixin = new AvailabilityMixin();
	
	public AvailabilityGuaranteeTerm(LinkType representation) {
		super(representation);
		
		// incarnation
		try {
			this.serviceEvaluatorLink = RepresentationBuilder.buildLinkRepresentation(representation, this.serviceEvaluatorLink);
			this.eventLogMixin = RepresentationBuilder.buildLinkRepresentation(representation, this.eventLogMixin);
			this.timeWindowMetricMixin = RepresentationBuilder.buildLinkRepresentation(representation, this.timeWindowMetricMixin);
			this.availabilityMixin = RepresentationBuilder.buildLinkRepresentation(representation, this.availabilityMixin);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.buildStatements(serviceEvaluatorLink, eventLogMixin, timeWindowMetricMixin, availabilityMixin.slo.toString());
	}

	@Override
	protected void violated(String value) {
		logger.info("The guarantee=" + this.getPath() + " is violated: Availability = " + value);
	}

	@Override
	protected void fulfilled(String value) {
		logger.info("The guarantee=" + this.getPath() + " is fulfilled: Availability = " + value);
	}
	
}
