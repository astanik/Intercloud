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
