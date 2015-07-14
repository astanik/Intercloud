package de.tu_berlin.cit.intercloud.occi.monitoring;

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
@Mixin(schema = MonitoringSchemas.MeterMixinSchema, term = AggregationMixin.AggregationTerm,
applies = MonitoringSchemas.MeterSchema + MeterKind.MeterTerm)
public class AggregationMixin extends Category{

    public final static String AggregationTitle = "Aggregation Mixin";
	
	public final static String AggregationTerm = "aggregation";
	
	public AggregationMixin(){
		super(AggregationTitle);
	}
	
	public AggregationMixin(String title){
		super(title);
	}
	
	/**
	 * A domain-specific String that describes the aggregation operation.
	 */
	@Attribute(name = "occi.meter.operation",
			type = AttributeType.STRING,
			mutable = false,
			required = true,
			description = "A domain-specific String that describes the aggregation operation.")
	public String operation = null	;
}
