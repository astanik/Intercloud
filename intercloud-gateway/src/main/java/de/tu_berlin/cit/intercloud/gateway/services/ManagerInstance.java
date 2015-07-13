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

package de.tu_berlin.cit.intercloud.gateway.services;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Classification;
import de.tu_berlin.cit.intercloud.occi.core.annotations.Summary;
import de.tu_berlin.cit.intercloud.occi.sla.ManagerKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.PathID;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
@PathID
@Summary("This resource allows for manage overall tasks, "
		+ "espesially agreement management, monitoring, "
		+ "and virtual machine instantiation.")
@Classification(kind = ManagerKind.class)
public class ManagerInstance extends Resource {

	private final PrivateKey privKey;

	private final PublicKey pubKey;

	public ManagerInstance(OcciXml occiRepresentation) {
		super(occiRepresentation);

		// Generate a key pair
		KeyPair pair = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance("DSA", "SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);

			pair = keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pair != null) {
				this.privKey = pair.getPrivate();
				this.pubKey = pair.getPublic();
			} else {
				this.privKey = null;
				this.pubKey = null;
			}
		}

		// Save the public key in the representation
		byte[] key = this.pubKey.getEncoded();
		// TODO
	}

	private byte[] signRepresentation() throws NoSuchAlgorithmException,
			NoSuchProviderException, InvalidKeyException, SignatureException {
		// Create a Signature object and initialize it with the private key
		Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");

		dsa.initSign(this.privKey);

		// Get Agreement data
		String agreement = this.getRepresentation().toString();

		// Update and sign the data
		byte[] buffer = agreement.getBytes();
		dsa.update(buffer);

		// generate a signature for it
		return dsa.sign();
	}

	private boolean verifyRepresentation(byte[] signatureToVerify)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			InvalidKeySpecException, SignatureException, InvalidKeyException {
		byte[] key = this.pubKey.getEncoded();
		// encode public key
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

		// create a Signature object and initialize it with the public key
		Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
		sig.initVerify(pubKey);

		// Get Agreement data
		String agreement = this.getRepresentation().toString();

		// Update and sign the data
		byte[] buffer = agreement.getBytes();
		sig.update(buffer);

		return sig.verify(signatureToVerify);
	}
}
