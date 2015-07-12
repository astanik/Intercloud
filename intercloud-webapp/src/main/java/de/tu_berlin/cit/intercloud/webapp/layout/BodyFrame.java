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


import java.io.Serializable;
import java.util.ArrayList;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;

import de.tu_berlin.cit.intercloud.webapp.content.ConnectedUsersPage;
import de.tu_berlin.cit.intercloud.webapp.content.CredentialsPage;


/**
 * Frame that contains the page main content.
 * 
 * @author Alexander Stanik
 */
@SuppressWarnings("serial")
public class BodyFrame extends WebPage {

	/**
	 * Class for defining a list of navigation items.
	 */
	public class NavigationItem implements Serializable {
		
		private final String pageName;
		
		private final Class<? extends BasePage> pageClass;
		
		public NavigationItem(String pageName, Class<? extends BasePage> pageClass) {
			this.pageName = pageName;
			this.pageClass = pageClass;
		}
		
		public final String getPageName() {
			return pageName;
		}
		
		public final Class<? extends BasePage> getPageClass() {
			return pageClass;
		}
		
	}
	
	/**
	 * Page that will be displayed
	 */
	private final TargetFrame frameTarget;

	/**
	 * the navigation frame
	 */
	private final NavigationFrame leftFrame;

	/**
	 * Constructor
	 */
	public BodyFrame() {
		frameTarget = new TargetFrame(DefaultPage.class);
		leftFrame = new NavigationFrame(this);
		getSession().getPageManager().touchPage(leftFrame);
		IRequestHandler leftFrameHandler = new RenderPageRequestHandler(new PageProvider(leftFrame));
		ExtendedFrame leftFrameTag = new ExtendedFrame("leftFrame", leftFrameHandler);
		add(leftFrameTag);

		ExtendedFrame rightFrameTag = new ExtendedFrame("rightFrame") {
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getUrl() {
				return frameTarget.getUrl();
			}
		};
		add(rightFrameTag);
	}

	/**
	 * Gets frameTarget.
	 * 
	 * @return frameTarget
	 */
	public TargetFrame getFrameTarget() {
		return frameTarget;
	}

	/**
	 * @see org.apache.wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned() {
		return false;
	}

	/**
	 * refreshes the current page.
	 */
	public void refresh() {
		this.setResponsePage(leftFrame);
	}

	protected ArrayList<NavigationItem> getUserNavigationList() {
		ArrayList<NavigationItem> list = new ArrayList<NavigationItem>();
		list.add(new NavigationItem("Credentials", CredentialsPage.class));
		//list.add(new NavigationItem("CM Connection", CloudManagerPage.class));
		return list;
	}

	protected ArrayList<NavigationItem> getAdminNavigationList() {
		ArrayList<NavigationItem> list = new ArrayList<NavigationItem>();
		list.add(new NavigationItem("Connected Users", ConnectedUsersPage.class));
        //list.add(new NavigationItem("Activated Resources", ActivatedResourcesPage.class));
        //list.add(new NavigationItem("Profiles",ProfilePage.class));
        //list.add(new NavigationItem("Nodes", NodesPage.class));
        return list;
	}

}