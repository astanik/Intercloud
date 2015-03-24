/**
 * Copyright (C) 2012-2015 TU Berlin. All rights reserved.
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

package de.tu_berlin.cit.intercloud.webapp.content;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import de.tu_berlin.cit.intercloud.webapp.layout.BasePage;


/**
 * The web page that will be displayed as "home page".
 * 
 * @author Alexander Stanik
 * 
 */
@AuthorizeInstantiation({ "USER", "ADMIN" })
@SuppressWarnings("serial")
public class ConnectedUsersPage extends BasePage {

	// DOCUMENT_ME: description
	public ConnectedUsersPage() {
		
		// do nothing
	}
	

}
