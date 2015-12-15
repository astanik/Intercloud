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

package de.tu_berlin.cit.intercloud.xmpp.client.extension;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.IQProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class XwadlIQProvider extends IQProvider<XwadlIQ> {
	private static Logger logger = LoggerFactory.getLogger(XwadlIQProvider.class);

	@Override
	public XwadlIQ parse(XmlPullParser parser, int initialDepth)
			throws XmlPullParserException, IOException, SmackException {
		long start = System.currentTimeMillis();
		// convert to string
		String str = XmlPullParserConverter.convert2String(parser);
		// create xwadl
		try {
			ResourceTypeDocument xwadl = ResourceTypeDocument.Factory.parse(str);
			logger.info("XML --> XmlBean: {} ms", System.currentTimeMillis() - start);
			return new XwadlIQ(xwadl);
		} catch (XmlException e) {
			throw new XmlPullParserException(e.getMessage());
		}
	}

}
