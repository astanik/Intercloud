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

package de.tu_berlin.cit.intercloud.exchange;

import de.tu_berlin.cit.intercloud.util.constants.ServiceNames;
import de.tu_berlin.cit.intercloud.xmpp.component.ResourceContainerComponent;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public class ExchangeComponent extends ResourceContainerComponent {
	
	
	public ExchangeComponent(ResourceContainer container) {
		super(container);
	}

	@Override
	public String getName() {
		return ServiceNames.ExchangeComponentName;
	}

	@Override
	public String getDescription() {
		return "This is the Intercloud Exchange service.";
	}

	@Override
	protected void handleRestXWADL(ResourceTypeDocument parse) {
		// TODO Auto-generated method stub
		logger.info("handleRestXWADL");
	}

	@Override
	protected void handleRestXML(ResourceDocument parse) {
		// TODO Auto-generated method stub
		logger.info("handleRestXML");
	}


//	public void postComponentStart() {
//		String domain = this.getDomain().replace("exchange.", "");
//	}



}
