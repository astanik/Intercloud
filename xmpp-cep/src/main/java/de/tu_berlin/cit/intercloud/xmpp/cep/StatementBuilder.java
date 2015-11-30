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

package de.tu_berlin.cit.intercloud.xmpp.cep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

import de.tu_berlin.cit.intercloud.occi.sla.LengthWindowMetricMixin;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceEvaluatorLink;
import de.tu_berlin.cit.intercloud.occi.sla.ServiceEvaluatorLink.RelationalOperator;
import de.tu_berlin.cit.intercloud.occi.sla.TimeWindowMetricMixin;
import de.tu_berlin.cit.intercloud.xmpp.cep.mixins.EventLogMixin;

/**
 * This static class builds Esper statements based on mixins and links. 
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class StatementBuilder {

	protected final static Logger logger = LoggerFactory.getLogger(StatementBuilder.class);

	public static EPStatement buildViolationStatement(ServiceEvaluatorLink evaluator, EventLogMixin eventLog,
			TimeWindowMetricMixin timeWindow, String slo) {
		logger.info("Start building time window violation statement ...");
		
		// create the select clause
		String expression = buildSelect(evaluator, eventLog);
		// set the event stream
		expression = expression + buildFrom(eventLog);
		// set time window
		expression = expression + buildTimeWindow(timeWindow);
		// build where clause
		expression = expression + buildViolationWhere(evaluator, slo);
		
		// create new statement
		EPServiceProvider epService = ComplexEventProcessor.getInstance().getProvider();
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);
		
		logger.info("Finished building time window violation statement: " + expression);
		return statement;
	}

	public static EPStatement buildFulfillStatement(ServiceEvaluatorLink evaluator, EventLogMixin eventLog,
			TimeWindowMetricMixin timeWindow, String slo) {
		logger.info("Start building time window fulfill statement ...");
		
		// create the select clause
		String expression = buildSelect(evaluator, eventLog);
		// set the event stream
		expression = expression + buildFrom(eventLog);
		// set time window
		expression = expression + buildTimeWindow(timeWindow);
		// build where clause
		expression = expression + buildFulfillWhere(evaluator, slo);
		
		// create new statement
		EPServiceProvider epService = ComplexEventProcessor.getInstance().getProvider();
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);
		
		logger.info("Finished building time window fulfill statement: " + expression);
		return statement;
	}

	public static EPStatement buildViolationStatement(ServiceEvaluatorLink evaluator, EventLogMixin eventLog,
			LengthWindowMetricMixin lengthWindow, String slo) {
		logger.info("Start building length window violation statement ...");
		
		// create the select clause
		String expression = buildSelect(evaluator, eventLog);
		// set the event stream
		expression = expression + buildFrom(eventLog);
		// set time window
		expression = expression + buildLengthWindow(lengthWindow);
		// build where clause
		expression = expression + buildViolationWhere(evaluator, slo);
		
		// create new statement
		EPServiceProvider epService = ComplexEventProcessor.getInstance().getProvider();
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);
		
		logger.info("Finished building length window violation statement: " + expression);
		return statement;
	}

	public static EPStatement buildFulfillStatement(ServiceEvaluatorLink evaluator, EventLogMixin eventLog,
			LengthWindowMetricMixin lengthWindow, String slo) {
		logger.info("Start building length window fulfill statement ...");
		
		// create the select clause
		String expression = buildSelect(evaluator, eventLog);
		// set the event stream
		expression = expression + buildFrom(eventLog);
		// set time window
		expression = expression + buildLengthWindow(lengthWindow);
		// build where clause
		expression = expression + buildFulfillWhere(evaluator, slo);
		
		// create new statement
		EPServiceProvider epService = ComplexEventProcessor.getInstance().getProvider();
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);
		
		logger.info("Finished building length window fulfill statement: " + expression);
		return statement;
	}

	

	private static String buildSelect(ServiceEvaluatorLink evaluator, EventLogMixin eventLog) {
		String expression = "select ";
		if(evaluator.aggregationOperator != null) {
			expression = expression + evaluator.aggregationOperator.toString() + "(";
			expression = expression + eventLog.getTag();
			expression = expression + ")";
		} else {
			expression = expression + eventLog.getTag();
		}
		// create alias
		expression = expression + " as slo";

		return expression;
	}	
	
	private static String buildFrom(EventLogMixin eventLog) {
		return buildFrom(eventLog.getStream());
	}

	/**
	 * Returns a from clause for a specific event stream
	 * @param eventStream The particular event stream
	 * @return The from clause
	 */
	private static String buildFrom(String eventStream) {
		return " from " + eventStream;
	}
	
	/**
	 * Returns a length window clause
	 * @param mixin The mixin that defines the window
	 * @return The window clause
	 */
	private static String buildLengthWindow(LengthWindowMetricMixin mixin) {
		if(LengthWindowMetricMixin.WindowType.SlidingWindow.equals(mixin.windowType))
			return ".win:length(" + mixin.windowLength + ")";
		else if(LengthWindowMetricMixin.WindowType.BatchWindow.equals(mixin.windowType))
			return ".win:length_batch(" + mixin.windowLength + ")";
		else
			return "";
	}
	
	/**
	 * Returns a time window clause
	 * @param mixin The mixin that defines the window
	 * @return The window clause
	 */
	private static String buildTimeWindow(TimeWindowMetricMixin mixin) {
		if(TimeWindowMetricMixin.WindowType.SlidingWindow.equals(mixin.windowType))
			return ".win:time(" + mixin.windowDuration + " " + mixin.durationUnit.toString() + ")";
		else if(TimeWindowMetricMixin.WindowType.BatchWindow.equals(mixin.windowType))
			return ".win:time_batch(" + mixin.windowDuration + " " + mixin.durationUnit.toString() + ")";
		else
			return "";
	}

	private static String buildFulfillWhere(ServiceEvaluatorLink evaluator, String slo) {
		// create slo selection
		String expression = buildWhere(evaluator.getObject(), evaluator.getSubject());
		if(slo != null && evaluator.relationalOperator != null)
			return expression + " and " + buildFulfillExpression(slo, evaluator.relationalOperator);
		else
			return expression;
	}

	private static String buildFulfillExpression(String slo, RelationalOperator relationalOperator) {
		String expression = "slo ";
		switch(relationalOperator) {
		case LESS_THAN:
			expression = expression + "<";
			break;
		case LESS_THAN_OR_EQUAL_TO:
			expression = expression + "<=";
			break;
		case EQUAL_TO:
			expression = expression + "=";
			break;
		case NOT_EQUAL_TO:
			expression = expression + "!=";
			break;
		case GREATER_THAN_OR_EQUAL_TO:
			expression = expression + ">=";
			break;
		case GREATER_THAN:
			expression = expression + ">";
			break;
		default:
			throw new RuntimeException(
					"Undefined Relational Operator");
		}
		return expression + " " + slo;
	}

	private static String buildViolationWhere(ServiceEvaluatorLink evaluator, String slo) {
		// create slo selection
		String expression = buildWhere(evaluator.getObject(), evaluator.getSubject());
		if(slo != null && evaluator.relationalOperator != null)
			return expression + " and " + buildViolationExpression(slo, evaluator.relationalOperator);
		else
			return expression;
	}

	private static String buildViolationExpression(String slo, RelationalOperator relationalOperator) {
		String expression = "slo ";
		switch(relationalOperator) {
		case LESS_THAN:
			expression = expression + ">=";
			break;
		case LESS_THAN_OR_EQUAL_TO:
			expression = expression + ">";
			break;
		case EQUAL_TO:
			expression = expression + "!=";
			break;
		case NOT_EQUAL_TO:
			expression = expression + "=";
			break;
		case GREATER_THAN_OR_EQUAL_TO:
			expression = expression + "<";
			break;
		case GREATER_THAN:
			expression = expression + "<=";
			break;
		default:
			throw new RuntimeException(
					"Undefined Relational Operator");
		}
		return expression + " " + slo;
	}

	/**
	 * Returns the where clause for a specific sensor and a specific subject
	 * @param sensorPath The sensor's full jid
	 * @param subjectPath The subject's full jid
	 * @return The where clause
	 */
	private static String buildWhere(String sensorPath, String subjectPath) {
		// create subject selection
		String expression = buildWhere(sensorPath);
		return expression + " and subject = '" + subjectPath + "'";
	}

	/**
	 * Returns the where clause for a specific sensor
	 * @param sensorPath The sensor's full jid
	 * @return The where clause
	 */
	private static String buildWhere(String sensorPath) {
		// create sensor selection
		return " where object = '" + sensorPath + "'";
	}


}
