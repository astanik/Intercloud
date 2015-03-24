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

package de.tu_berlin.cit.intercloud.webapp.auth;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.Parameter;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;

/**
 * This class have static methods for OpenID registration activities.
 * 
 * Most of this code based on the openid4java sample code.
 * 
 * @see http://code.google.com/p/openid4java/wiki/SampleConsumer
 * @see http://code.google.com/p/openid4java/wiki/AttributeExchangeHowTo
 * @see http://code.google.com/p/openid4java/wiki/SRegHowTo
 * 
 * @author Alexander Stanik
 * 
 */
public class OpenIdService {
	
	private static final Logger log = Logger.getLogger(OpenIdService.class);
	
	/**
	 * Perform discovery on the User-Supplied identifier and return the
	 * DiscoveryInformation object that results from Association with the
	 * OP.
	 * 
	 * @param userSuppliedIdentifier The User-Supplied identifier. It may already
	 *  be normalized.
	 *
	 * @return DiscoveryInformation The resulting DisoveryInformation object
	 *  returned by openid4java following successful association with the OP.
	 */
	@SuppressWarnings("unchecked")
	public static DiscoveryInformation performDiscoveryOnUserSuppliedIdentifier(String userSuppliedIdentifier) {
		ConsumerManager consumerManager = getConsumerManager();
		try {
			// Perform discover on the User-Supplied Identifier
			List<DiscoveryInformation> discoveries = consumerManager.discover(userSuppliedIdentifier);
			// Pass the discoveries to the associate() method...
			return consumerManager.associate(discoveries);

		} catch (DiscoveryException e) {
			String message = "Error occurred during discovery!";
			log.error(message, e);
			throw new RuntimeException(message, e);
		}
	}
	
	/**
	 * Create an OpenID Auth Request, using the DiscoveryInformation object
	 * return by the openid4java library.
	 * 
	 * This method uses not only the Simple Registration Extension but also 
	 * the Attribute Exchange Extension to grant the Relying Party (RP).
	 * 
	 * @param discoveryInformation The DiscoveryInformation that should have
	 *  been previously obtained from a call to performDiscoveryOnUserSuppliedIdentifier().
	 *  
	 * @param returnToUrl The URL to which the OP will redirect once the
	 *   authentication call is complete.
	 *  
	 * @return AuthRequest A "good-to-go" AuthRequest object packed with all
	 *  kinds of great OpenID goodies for the OpenID Provider (OP). The caller
	 *  must take this object and forward it on to the OP.
	 */
	public static AuthRequest createOpenIdAuthRequest(DiscoveryInformation discoveryInformation, String returnToUrl) {
		try {
			// Create the AuthRequest object
			AuthRequest ret = getConsumerManager().authenticate(discoveryInformation, returnToUrl);
			// Create the Simple Registration Request
			ret.addExtension(createSimpleRegistrationExtension());
			// Create the Attribute Exchange Request
			ret.addExtension(createAttributeExchangeExtension());
			
			return ret;
			
		} catch (Exception e) {
			String message = "Exception occurred while building AuthRequest object!";
			log.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	/**
	 * Returns a Simple Registration Extension for full name and email address.
	 * 
	 */
	private static SRegRequest createSimpleRegistrationExtension() {
		SRegRequest sRegRequest = SRegRequest.createFetchRequest();
		sRegRequest.addAttribute("fullname", true);
		sRegRequest.addAttribute("email", true);
		return sRegRequest;
	}
	
	/**
	 * Returns a Attribute Exchange Extension for first name, last name and one email address.
	 * 
	 */
	private static FetchRequest createAttributeExchangeExtension() throws MessageException {
		FetchRequest fetch = FetchRequest.createFetchRequest();
		fetch.addAttribute("FirstName", "http://schema.openid.net/namePerson/first", true);
		fetch.addAttribute("LastName", "http://schema.openid.net/namePerson/last", true);
		fetch.addAttribute("Email", "http://schema.openid.net/contact/email", true);
		// wants up to one email address
		fetch.setCount("Email", 1);
		return fetch;
	}

	/**
	 * Processes the returned information from an authentication request
	 * from the OP.
	 * 
	 * @param discoveryInformation DiscoveryInformation that was created earlier
	 *  in the conversation (by openid4java). This will need to be verified with
	 *  openid4java to make sure everything went smoothly and there are no
	 *  possible problems.
	 *  
	 * @param pageParameters PageParameters passed to the page handling the
	 *  return verification.
	 *  
	 * @param returnToUrl The "return to" URL that was passed to the OP. It must
	 *  match exactly, or openid4java will issue a verification failed message
	 *  in the logs.
	 *  
	 * @return OpenIdUser - An empty object if there was a problem, or an
	 *  object with parameters filled in as completely as possible from the
	 *  information available from the OP.
	 */
	public static OpenIdUser processReturn(DiscoveryInformation discoveryInformation, PageParameters pageParameters, String returnToUrl) {
		OpenIdUser ret = new OpenIdUser();
		ParameterList response = convertWicketPageParameters(pageParameters);
		// Verify the Information returned from the OP
		try {
			VerificationResult verificationResult = getConsumerManager().verify(returnToUrl, response, discoveryInformation);
			Identifier verifiedIdentifier = verificationResult.getVerifiedId();
			if (verifiedIdentifier != null) {
				ret.setOpenId(verifiedIdentifier.getIdentifier());
				AuthSuccess authSuccess = (AuthSuccess)verificationResult.getAuthResponse();
				ret = getValuesFromMessageExtension(ret, authSuccess);
			}
		} catch (Exception e) {
			String message = "Exception occurred while verifying response!";
			log.error(message, e);
			throw new RuntimeException(message, e);
		}
		return ret;
	}
	
	private static ParameterList convertWicketPageParameters(final PageParameters pageParameters) {
		// create parameter list
		ParameterList response = new ParameterList();
		// get key set
		Set<String> keys = pageParameters.getNamedKeys();
		Iterator<String> iter = keys.iterator();
		// copy all keys and values
		while (iter.hasNext()) {
			String theKey = iter.next();
			String theValue = pageParameters.get(theKey).toString();
			response.set(new Parameter(theKey, theValue));
		}
		// Log parameter list
		log.info(pageParameters.toString());
		return response;
	}
	
	private static OpenIdUser getValuesFromMessageExtension(OpenIdUser user, final AuthSuccess auth) throws MessageException {
		// for Simple Registration Extension
		if (auth.hasExtension(SRegMessage.OPENID_NS_SREG)) {
			MessageExtension extension = auth.getExtension(SRegMessage.OPENID_NS_SREG);
			if (extension instanceof SRegResponse) {
				SRegResponse sRegResponse = (SRegResponse)extension;
				String value = sRegResponse.getAttributeValue("email");
				if (value != null) {
				  user.setEmailAddress(value);
				}
				value = sRegResponse.getAttributeValue("fullname");
				if (value != null) {
					user.setFullName(value);
				}
			}
		} 
		// for Attribute Exchange Extension
		else if(auth.hasExtension(AxMessage.OPENID_NS_AX)) {
			MessageExtension extension = auth.getExtension(AxMessage.OPENID_NS_AX);

		    if (extension instanceof FetchResponse)
		    {
		        FetchResponse fetchResp = (FetchResponse)extension;
		        
		        String firstName = fetchResp.getAttributeValue("FirstName");
		        String lastName = fetchResp.getAttributeValue("LastName");
				if (firstName != null && lastName != null) {
					user.setFullName(firstName + " " + lastName);
				}
		        
		        // can have multiple values
		        List<?> emails = fetchResp.getAttributeValues("Email");
		        if(emails != null) {
		        	if(!emails.isEmpty())
		        		user.setEmailAddress(emails.get(0).toString());
		        }
		    }

		}
		return user;
	}

	private static ConsumerManager consumerManager;
	/**
	 * Retrieves an instance of the ConsumerManager object. It is static
	 * (see note in Class-level JavaDoc comments above) because openid4java
	 * likes it that way.
	 * 
	 * @return ConsumerManager - The ConsumerManager object that handles
	 *  communication with the openid4java API.
	 */
	private static ConsumerManager getConsumerManager() {
		if (consumerManager == null) {
			consumerManager = new ConsumerManager();
			consumerManager.setAssociations(new InMemoryConsumerAssociationStore());
			consumerManager.setNonceVerifier(new InMemoryNonceVerifier(10000));
		}
		return consumerManager;
	}
	
	/**
	 * Static returnToUrl parameter that is passed to the OP. The
	 * User Agent (i.e., the browser) will be directed to this page following
	 * authentication.
	 * 
	 * @TODO Generate this url by passing a target wicket page.
	 *  
	 * @return String - the returnToUrl to be used for the authentication request.
	 */
	public static String getReturnToUrl() {
		return "http://localhost:8080/wicket/bookmarkable/de.tu_berlin.cit.intercloud.webapp.auth.UserRegistrationPage?is_return=true";
	}
	
}
