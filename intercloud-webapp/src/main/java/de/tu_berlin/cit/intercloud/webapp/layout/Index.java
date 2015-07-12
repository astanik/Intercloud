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

package de.tu_berlin.cit.intercloud.webapp.layout;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;

/**
 * Index page of the web site.
 * 
 * @author Alexander Stanik
 * 
 */
@AuthorizeInstantiation({ "USER", "ADMIN" })
@SuppressWarnings("serial")
public class Index extends WebPage {

	/**
	 * Constructor
	 */
	public Index() {
		IRequestHandler topFrameHandler = new BookmarkablePageRequestHandler(new PageProvider(TopFrame.class));
		ExtendedFrame topFrame = new ExtendedFrame("topFrame", topFrameHandler);
		add(topFrame);

		IRequestHandler bodyFrameHandler = new BookmarkablePageRequestHandler(new PageProvider(BodyFrame.class));
		ExtendedFrame bodyFrame = new ExtendedFrame("bodyFrame", bodyFrameHandler);
		add(bodyFrame);
	}

}