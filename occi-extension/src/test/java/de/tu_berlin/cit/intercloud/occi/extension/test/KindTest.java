package de.tu_berlin.cit.intercloud.occi.extension.test;

import org.junit.Test;

import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.occi.servicecatalog.ServiceCatalogKind;

public class KindTest {

	@Test
	public void computeKindTest() {
		ComputeKind kind = new ComputeKind();
		System.out.println(kind.toString());
	}
	
	@Test
	public void catalogKindTest() {
		ServiceCatalogKind kind = new ServiceCatalogKind();
		System.out.println(kind.toString());
	}
	
}
