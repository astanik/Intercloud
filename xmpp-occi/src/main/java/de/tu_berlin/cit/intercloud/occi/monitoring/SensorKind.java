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

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;

@Kind(schema = SensorKind.SensorSchema, term = SensorKind.SensorTerm)
public class SensorKind extends Category {

	public final static String SensorTitle = "Sensor Resource";
	
	public final static String SensorSchema = "http://schema.cit.tu-berlin.de/occi/monitoring#";

	public final static String SensorMixinSchema = "http://schema.cit.tu-berlin.de/occi/monitoring/sensor#";

	public final static String SensorTerm = "sensor";
	
	public SensorKind() {
		super(SensorTitle);
	}

	public SensorKind(String title) {
		super(title);
	}

	@Attribute(name = "occi.sensor.lastmeasurand",
			type = AttributeType.FLOAT,
			description = "Current measurand of the last sample.")
	public Float lastMeasurand;
	

}
