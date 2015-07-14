package de.tu_berlin.cit.intercloud.occi.core.incarnation;

import java.util.HashMap;
import java.util.Map;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.occi.infrastructure.InfrastructureSchemas;
import de.tu_berlin.cit.intercloud.occi.infrastructure.NetworkKind;
import de.tu_berlin.cit.intercloud.occi.infrastructure.StorageKind;
import de.tu_berlin.cit.intercloud.occi.monitoring.MeterKind;
import de.tu_berlin.cit.intercloud.occi.monitoring.SensorKind;
import de.tu_berlin.cit.intercloud.occi.sla.AgreementKind;
import de.tu_berlin.cit.intercloud.occi.sla.ManagerKind;
import de.tu_berlin.cit.intercloud.occi.sla.OfferKind;

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
		mapping.put(SensorKind.SensorSchema + SensorKind.SensorTerm,
				SensorKind.class);
		mapping.put(MeterKind.MeterSchema + MeterKind.MeterTerm,
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
