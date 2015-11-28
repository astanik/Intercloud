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

import de.tu_berlin.cit.intercloud.occi.core.IntercloudSchemas;

/**
 * Constants definition for the CIT SLA Module.
 * 
 * -sla Kinds/Links ---SensorKind -----ActiveSensorMixin -----PassiveSensorMixin
 * ---MeterKind -----AggregationMixin ---CollectorLink -----MetricMixin
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */

public class SlaSchemas {

	public final static String SlaTag = "sla";

	public final static String SlaExtension = IntercloudSchemas.CitSchemaURL + SlaTag;

	public final static String SlaSchema = SlaExtension + "#";

	// Mixin Predefinition
	public final static String AgreementMixinSchema = SlaExtension + "/" + AgreementKind.AgreementTerm + "#";

	public final static String OfferMixinSchema = SlaExtension + "/" + OfferKind.OfferTerm + "#";

	public final static String ManagerMixinSchema = SlaExtension + "/" + ManagerKind.ManagerTerm + "#";

	public final static String ServiceReferenceMixinSchema = SlaExtension + "/"
			+ ServiceReferenceLink.ServiceReferenceTerm + "#";

	public final static String ServiceEvaluatorMixinSchema = SlaExtension + "/"
			+ ServiceEvaluatorLink.ServiceEvaluatorTerm + "#";

}
