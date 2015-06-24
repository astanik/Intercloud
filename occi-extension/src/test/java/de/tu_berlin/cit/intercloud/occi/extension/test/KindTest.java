package de.tu_berlin.cit.intercloud.occi.extension.test;

import org.junit.Test;

import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;

public class KindTest {

	@Test
	public void computeKindTest() {
		ComputeKind kind = new ComputeKind();
		System.out.println(kind.toString());
	}
	
}
