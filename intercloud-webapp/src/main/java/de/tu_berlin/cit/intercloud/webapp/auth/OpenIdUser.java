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


@SuppressWarnings("serial")
public class OpenIdUser implements User {

	private Roles roles;
	
	private String openId;

	private String fullName;
	
	private String emailAddress;

	
	@Override
	public void setRoles(Roles roles) {
		this.roles = roles;
	}
	
	@Override
	public Roles getRoles() {
		return this.roles;
	}
	
	@Override
	public void setUserName(String name) {
		this.setOpenId(name);
	}
	
	@Override
	public String getUserName() {
		return this.getOpenId();
	}

	public String getOpenId() {
		return openId;
	}
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
