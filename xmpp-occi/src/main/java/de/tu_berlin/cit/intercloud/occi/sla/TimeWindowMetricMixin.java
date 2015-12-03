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

package de.tu_berlin.cit.intercloud.occi.sla;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Mixin;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;

/**
 * This mixin defines a window with a fix time length.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Mixin(schema = SlaSchemas.ServiceEvaluatorMixinSchema, term = TimeWindowMetricMixin.TimeWindowMetricTerm,
		applies = SlaSchemas.SlaSchema + ServiceEvaluatorLink.ServiceEvaluatorTerm)
public class TimeWindowMetricMixin extends Category {

	public final static String TimeWindowMetricTitle = "Time Window Metric Mixin";
	
	public final static String TimeWindowMetricTerm = "timewindow";
	
	public TimeWindowMetricMixin() {
		super(TimeWindowMetricTitle);
	}

	public TimeWindowMetricMixin(String title) {
		super(title);
	}
	
	public enum WindowType {
		SlidingWindow,
		BatchWindow
	}
	
	/**
	 * The type of the window, i.e. either a periodically assessed window or a continuously assessed window.
	 */
	@Attribute(name = "intercloud.sla.metric.windowtype",
			type = AttributeType.ENUM,
			mutable = true,
			required = true,
			value = "SlidingWindow",
			description = "The type of the window, i.e. either a periodically assessed window (BatchWindow) or a continuously assessed window (SlidingWindow).")
	public WindowType windowType = WindowType.SlidingWindow;

	/**
	 * The time length of the window, i.e. the duration that this window is assess.
	 */
	@Attribute(name = "intercloud.sla.metric.windowduration",
			type = AttributeType.INTEGER,
			mutable = true,
			required = true,
			description = "The time length of the window, i.e. the duration that this window is assess.")
	public Integer windowDuration = null;

	public enum TimeUnit {
		years,
		months,
		weeks,
		days,
		hours,
		minutes,
		seconds,
		milliseconds
	}
	
	/**
	 * The unit in which time length of the window is specified.
	 */
	@Attribute(name = "intercloud.sla.metric.durationunit",
			type = AttributeType.ENUM,
			mutable = true,
			required = true,
			value = "seconds",
			description = "The unit in which time length of the window is specified (e.g. years, months, weeks, days, hours, minutes, seconds, or milliseconds).")
	public TimeUnit durationUnit = TimeUnit.seconds;

}
