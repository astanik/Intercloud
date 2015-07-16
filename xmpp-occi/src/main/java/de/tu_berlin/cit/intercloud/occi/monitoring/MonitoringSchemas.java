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



/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 * @author Daniel Thilo Schroeder <daniel.schroeder@mailbox.tu-berlin.de>
 */

public class MonitoringSchemas {
	
		
		
		private final static String SchemaURL = "http://schema.ogf.org/occi/";
		
		private final static String MonitoringTag = "monitoring";
		
		private final static String MonitoringSchema = SchemaURL + MonitoringTag +"#";
		
		/**
		 * 
		 * -monitoring Kinds/Links
		 * ---SensorKind
		 * -----ActiveSensorMixin
		 * -----PassiveSensorMixin
		 * ---MeterKind
		 * -----AggregationMixin
		 * ---CollectorLink
		 */
		
		//Kinds/Links
		public final static String SensorSchema = MonitoringSchema;
		
		public final static String MeterSchema = MonitoringSchema;
	
		public final static String CollectorSchema = MonitoringSchema;		
		
		//Mixins
		public final static String SensorMixinSchema = SchemaURL + MonitoringTag + SensorKind.SensorTerm + "#";
		
		public final static String MeterMixinSchema = SchemaURL + MonitoringTag + MeterKind.MeterTerm + "#";
		
		public final static String CollectorMixinSchema = SchemaURL + MonitoringTag + CollectorLink.CollectorTerm + "#";

}
