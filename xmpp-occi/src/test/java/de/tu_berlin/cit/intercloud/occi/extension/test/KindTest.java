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

import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.ClassificationRegistry;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class KindTest {

	@Test
	public void registryTest() {
		// get all registered kinds, mixins, and links
		Map<String, Class<? extends Category>> classMapping;
		classMapping = ClassificationRegistry.getInstance().getClassMapping();
		// get a set of identifying schema and terms
		Set<String> schemata = classMapping.keySet();
		for(String schema : schemata) {
			Class<? extends Category> cls = classMapping.get(schema);
			try {
				Category cat = cls.newInstance();
				Assert.assertNotNull(cat);
				System.out.println(schema + " is mapped to " + cls.getName() 
						+ " and have by default: " + cat.toString());
			} catch (InstantiationException | IllegalAccessException e) {
				Assert.fail(e.getMessage());
			}
		}
	}
	
}
