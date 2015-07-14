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

import java.util.Calendar;

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
@Mixin(schema = MonitoringSchemas.SensorMixinSchema, term = PassiveSensorMixin.PassiveSensorTerm,
		applies = MonitoringSchemas.SensorSchema + SensorKind.SensorTerm)
public class PassiveSensorMixin extends Category {

	public final static String PassiveSensorTitle = "Active Sensor Mixin";
	
	public final static String PassiveSensorTerm = "passivesensor";
	
	public PassiveSensorMixin() {
		super(PassiveSensorTitle);
	}

	public PassiveSensorMixin(String title) {
		super(title);
	}
	
	/**
	 * Timestamp of the last occurrence of an sampling event.
	 */
	@Attribute(name = "occi.sensor.lastoccurrence",
			type = AttributeType.DATETIME,
			mutable = false,
			required = true,
			description = "Timestamp of the last occurrence of an sampling event.")
	public Calendar lastoccurrence = null;



}
