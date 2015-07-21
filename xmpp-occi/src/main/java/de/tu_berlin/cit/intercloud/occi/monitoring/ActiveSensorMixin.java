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
@Mixin(schema = MonitoringSchemas.SensorMixinSchema, term = ActiveSensorMixin.ActiveSensorTerm,
		applies = MonitoringSchemas.MonitoringSchema + SensorKind.SensorTerm)
public class ActiveSensorMixin extends Category {

	public final static String ActiveSensorTitle = "Active Sensor Mixin";
	
	public final static String ActiveSensorTerm = "activesensor";
	
	public ActiveSensorMixin() {
		super(ActiveSensorTitle);
	}

	public ActiveSensorMixin(String title) {
		super(title);
	}
	
	/**
	 * Sampling rate with which the sensor scans an object periodically.
	 */
	@Attribute(name = "occi.sensor.samplerate",
			type = AttributeType.DURATION,
			mutable = true,
			required = true,
			description = "Sampling rate with which the sensor scans an object periodically.")
	public GDuration samplerate = null;


}
