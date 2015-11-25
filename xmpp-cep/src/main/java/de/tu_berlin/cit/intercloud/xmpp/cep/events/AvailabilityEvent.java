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

import javax.xml.namespace.QName;

import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument;
import de.tu_berlin.cit.intercloud.xmpp.cep.eventlog.LogDocument.Log.Tag;

/**
 * Availability event that provides a double for the availability.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class AvailabilityEvent extends LogEvent {

	public static final String AvailabilityStream = "AvailabilityEvent";
	
	public static final String AvailabilityTag = "availability";

	private double availability;
	
	protected AvailabilityEvent(String object, String subject, Calendar timestamp, double availability) {
		super(object, subject, timestamp);
		this.availability = availability;
	}
	
	public double getAvailability() {
		return availability;
	}

	public void setAvailability(double availability) {
		this.availability = availability;
	}

	public static LogDocument build(String sensorPath, String subjectPath, double availability) {
		LogDocument event = LogEvent.build(sensorPath, subjectPath);
		// set event id
		event.getLog().setId(AvailabilityStream);
		
		// set availability
		Tag tag = event.getLog().addNewTag();
		tag.setName(AvailabilityTag);
		tag.setType(new QName("xs:double"));
		tag.setValue(new Double(availability).toString());
		
		logger.info("Finished building log event document: " + event.toString());
		return event;
	}

	public static AvailabilityEvent parse(LogDocument eventDoc) {
		String object = eventDoc.getLog().getObject();
		String subject = eventDoc.getLog().getSubject();
		Calendar timestamp = eventDoc.getLog().getTimestamp();
		Tag[] tags = eventDoc.getLog().getTagArray();
		double value = -1;
		
		// take the last matching tag
		for(Tag tag : tags) {
			if(tag.getName().equals(AvailabilityTag) && tag.getType().equals(new QName("xs:double")))
				value = Double.parseDouble(tag.getValue());
		}

		// TODO define exception
		if(value == -1)
			throw new RuntimeException("wrong tag declaration");
		
		return new AvailabilityEvent(object, subject, timestamp, value);
	}

}
