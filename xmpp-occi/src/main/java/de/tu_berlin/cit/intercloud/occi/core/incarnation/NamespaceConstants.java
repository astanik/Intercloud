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

package de.tu_berlin.cit.intercloud.occi.core.incarnation;

import java.util.HashMap;
import java.util.Map;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.occi.infrastructure.InfrastructureSchemas;
import de.tu_berlin.cit.intercloud.occi.infrastructure.NetworkKind;
import de.tu_berlin.cit.intercloud.occi.infrastructure.StorageKind;
import de.tu_berlin.cit.intercloud.occi.monitoring.MeterKind;
import de.tu_berlin.cit.intercloud.occi.monitoring.MonitoringSchemas;
import de.tu_berlin.cit.intercloud.occi.monitoring.SensorKind;
import de.tu_berlin.cit.intercloud.occi.sla.AgreementKind;
import de.tu_berlin.cit.intercloud.occi.sla.ManagerKind;
import de.tu_berlin.cit.intercloud.occi.sla.OfferKind;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class NamespaceConstants {

	public static final Map<String, Class<? extends Category>> mapping;
	static {
		mapping = new HashMap<String, Class<? extends Category>>();

		// infrastructure classification
		mapping.put(InfrastructureSchemas.ComputeSchema + ComputeKind.ComputeTerm,
				ComputeKind.class);
		mapping.put(InfrastructureSchemas.NetworkSchema + NetworkKind.NetworkTerm,
				NetworkKind.class);
		mapping.put(InfrastructureSchemas.StorageSchema + StorageKind.StorageTerm,
				StorageKind.class);

		// monitoring classification
		mapping.put(MonitoringSchemas.SensorSchema + SensorKind.SensorTerm,
				SensorKind.class);
		mapping.put(MonitoringSchemas.MeterSchema + MeterKind.MeterTerm,
				MeterKind.class);

		// sla classification
		mapping.put(ManagerKind.ManagerSchema + ManagerKind.ManagerTerm,
				ManagerKind.class);
		mapping.put(OfferKind.OfferSchema + OfferKind.OfferTerm,
				OfferKind.class);
		mapping.put(
				AgreementKind.AgreementSchema + AgreementKind.AgreementTerm,
				AgreementKind.class);
	}
}