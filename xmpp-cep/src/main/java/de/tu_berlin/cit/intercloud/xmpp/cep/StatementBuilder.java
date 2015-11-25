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

import de.tu_berlin.cit.intercloud.xmpp.cep.events.CpuUtilizationEvent;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class StatementBuilder {

	protected final static Logger logger = LoggerFactory.getLogger(StatementBuilder.class);

	public static EPStatement build(String sensorPath, String subjectPath) {
		logger.info("Start building statement ...");

		// create new expression
		String expression = "select object, subject, timestamp, avg(utilization) from CpuUtilizationEvent.win:time(5 sec) ";
//		String expression = "select object, subject, timestamp, utilization from CpuUtilizationEvent.win:time(5 sec)";
//		String expression = "select object, subject, timestamp, tag[0].type, tag[0].name, tag[0].value from LogEvent.win:time(3 sec)";
//		String expression = "select object, subject, timestamp, tag[0].type, tag[0].name, tag[0].value, min(tag[0].value), avg(tag[0].value), max(tag[0].value) from LogEvent.win:time(3 sec)";
//		String expression = "select * from LogEvent";
		// create new statement
		EPServiceProvider epService = ComplexEventProcessor.getInstance().getProvider();
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);
		
		logger.info("Finished building statement: " + expression);
		return statement;
	}

	public static EPStatement buildCpuUtilization(String sensorPath, String subjectPath, int seconds) {
		logger.info("Start building statement ...");
		
		// create the select clause
		String expression = "select avg(" + CpuUtilizationEvent.CpuUtilizationTag + ")";
		// create alias
		expression = expression + " as AverageUtilization";
		// set the event stream
		expression = expression + buildFrom(CpuUtilizationEvent.CpuUtilizationStream);
		// set time window
		expression = expression + ".win:time(" + seconds + " sec)";
		// build where clause
		expression = expression + buildWhere(sensorPath, subjectPath);
		
		
		
		// create new expression
//		String expression = "select object, subject, timestamp, avg(utilization) from CpuUtilizationEvent.win:time(5 sec) ";
//		String expression = "select object, subject, timestamp, utilization from CpuUtilizationEvent.win:time(5 sec)";
//		String expression = "select object, subject, timestamp, tag[0].type, tag[0].name, tag[0].value from LogEvent.win:time(3 sec)";
//		String expression = "select object, subject, timestamp, tag[0].type, tag[0].name, tag[0].value, min(tag[0].value), avg(tag[0].value), max(tag[0].value) from LogEvent.win:time(3 sec)";
//		String expression = "select * from LogEvent";
		// create new statement
		EPServiceProvider epService = ComplexEventProcessor.getInstance().getProvider();
		EPStatement statement = epService.getEPAdministrator().createEPL(expression);
		
		logger.info("Finished building statement: " + expression);
		return statement;
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
	 * Returns the where clause for a specific sensor and a specific subject
	 * @param sensorPath The sensor's full jid
	 * @param subjectPath The subject's full jid
	 * @return The where clause
	 */
	private static String buildWhere(String sensorPath, String subjectPath) {
		// create subject selection
		String expression = buildWhere(sensorPath);
		return expression + "and subject = '" + subjectPath + "'";
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
