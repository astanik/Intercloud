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

package de.tu_berlin.cit.intercloud.xmpp.cep.events;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;
import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument.Log;

/**
 * Basis class for all event types. This class also provides the constant
 * string for the default event stream.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class LogEvent {

	protected static final Logger logger = LoggerFactory.getLogger(LogEvent.class);

	public static final String LogEventStream = "LogEvent";
	
	private String object;
	
	private String subject;
	
	private Calendar timestamp;
	
	protected LogEvent(String object, String subject, Calendar timestamp) {
		this.object = object;
		this.subject = subject;
		this.timestamp = timestamp;
	}
	
	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public static LogDocument build(String sensorPath, String subjectPath) {
		logger.info("Start building log event document ...");
		// create new document 
		LogDocument event = LogDocument.Factory.newInstance();
		Log log = event.addNewLog();
		// set sensor path
		log.setObject(sensorPath);
		// set subject path
		log.setSubject(subjectPath);
		// set timestamp
		log.setTimestamp(Calendar.getInstance());
		
		logger.info("Finished building basic log event document: " + event.toString());
		return event;
	}

	public static LogDocument build(String sensorPath, String subjectPath, String message) {
		LogDocument event = LogEvent.build(sensorPath, subjectPath);
		// set message
		event.getLog().setMessage(message);

		logger.info("Finished building log event document: " + event.toString());
		return event;
	}

}
