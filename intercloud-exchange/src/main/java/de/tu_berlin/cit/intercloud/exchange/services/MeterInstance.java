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

package de.tu_berlin.cit.intercloud.exchange.services;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.monitoring.AggregationMixin;
import de.tu_berlin.cit.intercloud.occi.monitoring.MeterKind;
import de.tu_berlin.cit.rwx4j.annotations.PathID;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Summary("This resource allows for manage meter resources "
		+ "used for agreement evaluation.")
@Classification(kind = MeterKind.class, 
		mixins = { AggregationMixin.class })
public class MeterInstance extends Resource {

	public MeterInstance(OcciXml occiRepresentation) {
		super(occiRepresentation);
		// TODO Auto-generated constructor stub
	}

}
