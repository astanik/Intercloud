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

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;
import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument.Log.Tag;

/**
 * CPU utilization event that provides a double for the availability.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class CpuUtilizationEvent extends LogEvent {

	public static final String CpuUtilizationStream = "CpuUtilizationEvent";
	
	public static final String CpuUtilizationTag = "utilization";

	private int utilization;
	
	protected CpuUtilizationEvent(String object, String subject, Calendar timestamp, int utilization) {
		super(object, subject, timestamp);
		this.setUtilization(utilization);
	}
	
	public double getUtilization() {
		return utilization;
	}

	public void setUtilization(int utilization) {
		this.utilization = utilization;
	}

	public static LogDocument build(String sensorPath, String subjectPath, double utilization) {
		LogDocument event = LogEvent.build(sensorPath, subjectPath);
		// set event id
		event.getLog().setId(CpuUtilizationStream);
		
		// set availability
		Tag tag = event.getLog().addNewTag();
		tag.setName(CpuUtilizationTag);
		tag.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "integer"));
		tag.setValue(new Double(utilization).toString());
		
		logger.info("Finished building log event document: " + event.toString());
		return event;
	}

	public static CpuUtilizationEvent parse(LogDocument eventDoc) {
		String object = eventDoc.getLog().getObject();
		String subject = eventDoc.getLog().getSubject();
		Calendar timestamp = eventDoc.getLog().getTimestamp();
		Tag[] tags = eventDoc.getLog().getTagArray();
		int value = -1;
		
		// take the last matching tag
		for(Tag tag : tags) {
			if(tag.getName().equals(CpuUtilizationTag) && tag.getType().equals(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "integer")))
				value = Integer.parseInt(tag.getValue());
		}

		// TODO define exception
		if(value == -1)
			throw new RuntimeException("wrong tag declaration");
		
		return new CpuUtilizationEvent(object, subject, timestamp, value);
	}

}
