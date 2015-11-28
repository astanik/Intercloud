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

package de.tu_berlin.cit.intercloud.occi.sla;

import java.util.Calendar;

import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Category;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Kind;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Attribute.AttributeType;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@Kind(schema = SlaSchemas.SlaSchema, term = AgreementKind.AgreementTerm)
public class AgreementKind extends Category {

	public final static String AgreementTitle = "Agreement Resource";
		
	public final static String AgreementTerm = "agreement";
	
	public AgreementKind() {
		super(AgreementTitle);
	}

	public AgreementKind(String title) {
		super(title);
	}

	/**
	 * The domain of the provider
	 */
	@Attribute(name = "intercloud.sla.agreement.provider",
			type = AttributeType.STRING,
			mutable = true,
			required = false,
			description = "The domain of the provider")
	public String providerDomain = null;

	/**
	 * The JID of the customer
	 */
	@Attribute(name = "intercloud.sla.agreement.customer",
			type = AttributeType.STRING,
			mutable = true,
			required = false,
			description = "The JID of the customer")
	public String customerJID = null;

	/**
	 * The customer's signature
	 */
	@Attribute(name = "intercloud.sla.agreement.customersignature",
			type = AttributeType.SIGNATURE,
			mutable = true,
			required = false,
			description = "The customer's signature")
	public byte[] customerSignature = null;

	/**
	 * The provider's signature
	 */
	@Attribute(name = "intercloud.sla.agreement.providersignature",
			type = AttributeType.SIGNATURE,
			mutable = true,
			required = false,
			description = "The provider's signature")
	public byte[] providerSignature = null;

	/**
	 * When the provider and the customer agreed at
	 */
	@Attribute(name = "intercloud.sla.agreement.agreedat",
			type = AttributeType.DATETIME,
			mutable = true,
			required = false,
			description = "When the the provider and the customer agreed at")
	public Calendar agreedAt = null;

	/**
	 * When the provider and the customer agreed from
	 */
	@Attribute(name = "intercloud.sla.agreement.agreedfrom",
			type = AttributeType.DATETIME,
			mutable = true,
			required = false,
			description = "When the provider and the customer agreed from")
	public Calendar agreedFrom = null;

	/**
	 * When the the provider and the customer agreed until
	 */
	@Attribute(name = "intercloud.sla.agreement.agreementuntil",
			type = AttributeType.DATETIME,
			mutable = true,
			required = false,
			description = "When the provider and the customer agreed until")
	public Calendar agreedUntil = null;

	public enum State {
		pending, 
		observed, 
		suspended
	}
	
	/**
	 * Current state of the instance
	 */
	@Attribute(name = "intercloud.sla.serviceevaluator.state",
			type = AttributeType.ENUM,
			mutable = false,
			required = true,
			description = "Current state of the instance: Enum{undefined, violated, fulfilled}")
	public State state = null;

	/**
	 * Human-readable explanation of the current instance state
	 */
	@Attribute(name = "intercloud.sla.serviceevaluator.message",
			type = AttributeType.STRING,
			mutable = false,
			required = false,
			description = "Human-readable explanation of the current instance state")
	public String message = null;

}
