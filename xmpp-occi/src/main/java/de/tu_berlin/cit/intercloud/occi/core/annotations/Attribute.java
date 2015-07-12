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

package de.tu_berlin.cit.intercloud.occi.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Target(java.lang.annotation.ElementType.FIELD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Attribute {

	enum AttributeType {
		STRING,
		ENUM,
		INTEGER,
		FLOAT,
		DOUBLE,
		BOOLEAN,
		URI,
		SIGNATURE,
		KEY,
		DATETIME,
		DURATION
	}
	
	/**
	 * 
	 * @return The attribute name
	 */
	String name();

	/**
	 * 
	 * @return The type of the attribute
	 */
	AttributeType type();

	/**
	 * 
	 * @return Returns true if this attribute is mutabile
	 */
	boolean mutable() default false;

	/**
	 * 
	 * @return Returns true if this attribute is required
	 */
	boolean required() default false;

	/**
	 * 
	 * @return The default value of this attribute
	 */
	String value() default "";

	/**
	 * 
	 * @return The description of this attribute
	 */
	String description() default "";

}
