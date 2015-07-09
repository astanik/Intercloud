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

import org.apache.wicket.util.io.IClusterable;
import org.apache.wicket.Page;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;

/**
 * Simple struct for holding the class of the right frame.
 * 
 * @author Alexander Stanik
 */
@SuppressWarnings("serial")
public final class TargetFrame implements IClusterable {

	/** the class of the bookmarkable page. */
	private Class<? extends Page> frameClass;

	/**
	 * Constructor.
	 */
	public TargetFrame() {
	}

	/**
	 * Constructor.
	 * 
	 * @param <C> Page class
	 * @param frameClass class
	 */
	public <C extends Page> TargetFrame(Class<C> frameClass) {
		this.frameClass = frameClass;
	}

	/**
	 * Gets frame class.
	 * 
	 * @param <?> Page class
	 * @return lefFrameClass
	 */
	public Class<? extends Page> getFrameClass() {
		return frameClass;
	}

	/**
	 * Sets frame class.
	 * 
	 * 
	 * @param <C> Page class
	 * @param frameClass
	 *            lefFrameClass
	 */
	public <C extends Page> void setFrameClass(Class<C> frameClass) {
		this.frameClass = frameClass;
	}

	/**
	 * @return URL to this frame class
	 */
	public CharSequence getUrl() {
		return RequestCycle.get().urlFor(new BookmarkablePageRequestHandler(new PageProvider(frameClass)));
	}
}