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

package de.tu_berlin.cit.intercloud.xmpp.rest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * This annotation can be applied to an XMPP method or an XMPP action.
 * It indicates at which states this method is be accessible or
 * rendered in an XWADL document.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Condition {

	/**
	 * The states at which this method is available.
	 * @return The states as array
	 */
	String[] value();
	
}