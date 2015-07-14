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

package de.tu_berlin.cit.intercloud.occi.monitoring;

import org.apache.xmlbeans.GDuration;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Mixin;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 * @author Daniel Thilo Schroeder <daniel.schroeder@mailbox.tu-berlin.de>
 */
@Mixin(schema = MonitoringSchemas.CollectorMixinSchema, term = MetricMixin.MetricSensorTerm,
		applies = MonitoringSchemas.CollectorSchema + CollectorLink.CollectorTerm)
public class MetricMixin extends Category {

	public final static String MetricSensorTitle = "Metric Mixin";
	
	public final static String MetricSensorTerm = "metric";
	
	public MetricMixin() {
		super(MetricSensorTitle);
	}

	public MetricMixin(String title) {
		super(title);
	}
	
	/**
	 * Time period in which the measurement is calculated.
	 */
	@Attribute(name = "occi.collector.period",
			type = AttributeType.DURATION,
			mutable = false,
			required = true,
			description = "Time period in which the measurement is calculated.")
	public GDuration period = null;
	
	public enum Method {
		continuously,
		periodically
	}
	
	/**
	 * The method indicates how often a measurement is calculated for a period of time.
	 */
	@Attribute(name = "occi.collector.method",
			type = AttributeType.ENUM,
			mutable = false,
			required = true,
			description = "The method indicates how often a measurement is calculated for a period of time.")
	public Method method = null;
	
	/**
	 * Policies define the algorithm with which the measurement is calculated.
	 */
	@Attribute(name = "occi.collector.policies",
			type = AttributeType.STRING,
			mutable = false,
			required = true,
			description = "Policies define the algorithm with which the measurement is calculated.")
	public String policies = null;


}
