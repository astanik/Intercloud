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
