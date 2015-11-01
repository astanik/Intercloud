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

package de.tu_berlin.cit.intercloud.webapp.auth;


import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.util.io.IClusterable;

public interface User extends IClusterable {

	/**
	 * sets the Wicket Roles for this user.
	 * 
	 * @param roles
	 *            the roles that belong to this user.
	 */
	public void setRoles(Roles roles);

	/**
	 * 
	 * @return the Wicket roles of this user
	 */
	public Roles getRoles();

	/**
	 * Set the user name.
	 * 
	 * @param name The user name.
	 */
	public void setUsername(String name);

	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUsername();


}
