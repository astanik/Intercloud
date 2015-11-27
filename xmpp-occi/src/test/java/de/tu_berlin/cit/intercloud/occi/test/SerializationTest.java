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

package de.tu_berlin.cit.intercloud.occi.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.ClassificationRegistry;

/**
 * Test the classification registry.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class SerializationTest {

	@Test
	public void serializationTest() {
		// test map
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "Tes, t1");
		map.put("2", "Test2");
		map.put("3", "Tes=t3");
		map.put("4", "Test4");
		map.put("5", "Test5");
		
		// create string representation
		String str = map.toString();
		System.out.println("Map as String:" + str);
		
		// use properties to restore the map
		Properties props = new Properties();
		try {
			props.load(new StringReader(str.substring(1, str.length() - 1).replace(", ", "\n")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}       
		Map<String, String> map2 = new HashMap<String, String>();
		for (Map.Entry<Object, Object> e : props.entrySet()) {
		    map2.put((String)e.getKey(), (String)e.getValue());
		    System.out.println("Key:" + (String)e.getKey() + " Value:" + (String)e.getValue());
		}
		System.out.println("The new Map as String:" + map2.toString());
		
		// test list
		List<String> list = new ArrayList<String>();
		list.add("Test1");
		list.add("Test2");
		list.add("Test3");
		list.add("Test4");
		list.add("Test5");
		
		System.out.println(list.toString());
	}
	
}
