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

import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

import de.tu_berlin.cit.intercloud.webapp.auth.LogoutPage;
import de.tu_berlin.cit.intercloud.webapp.layout.BodyFrame.NavigationItem;


/**
 * Frame used as page navigation panel.
 * 
 * @author Alexander Stanik
 */
@SuppressWarnings("serial")
public class NavigationFrame extends WebPage {

	/**
	 * Link that, when clicked, changes the frame target's frame class (and as
	 * that is a shared model which is also being used by the 'master page'
	 * {@link BodyFrame}, changes are immediately reflected) and set the
	 * response page to the top level page {@link BodyFrame}. Tags that use this
	 * link should have a <code>target="_parent"</code> attribute, so that the
	 * top frame will be refreshed.
	 */
	private static class ChangeFramePageLink extends Link<NavigationItem> {
		private static final long serialVersionUID = 1L;

		/** parent frame class. */
		private final BodyFrame bodyFrame;

		/** this link's target. */
		private final Class<? extends BasePage> pageClass;

		/**
		 * Construct.
		 * 
		 * @param <C>
		 * 
		 * @param id
		 * @param bodyFrame
		 * @param pageClass
		 */
		public <C extends BasePage> ChangeFramePageLink(String id, BodyFrame bodyFrame, Class<C> pageClass) {
			super(id);
			this.bodyFrame = bodyFrame;
			this.pageClass = pageClass;
		}

		/**
		 * @see org.apache.wicket.markup.html.link.Link#onClick()
		 */
		@Override
		public void onClick() {
			// change frame class
			bodyFrame.getFrameTarget().setFrameClass(pageClass);

			// trigger re-rendering of the page
			setResponsePage(bodyFrame);
		}
	}

	/**
	 * A panel that is only visible for users with role ADMIN.
	 */
	@AuthorizeAction(action = Action.RENDER, roles = Roles.ADMIN)
	private static class ForAdmins extends Panel {
		/**
		 * Construct.
		 * 
		 * @param id
		 */
		public ForAdmins(String id, final BodyFrame index) {
			super(id);
			
			// generate data-view to populate the table
			DataView<NavigationItem> naviView = new DataView<NavigationItem>("adminNavis",
					new ListDataProvider<NavigationItem>(index.getAdminNavigationList())) {

				@Override
				protected void populateItem(Item<NavigationItem> item) {
					NavigationItem naviItem = item.getModelObject();

					// link to the page
					ChangeFramePageLink link = new ChangeFramePageLink("linkToAdminPage", index, naviItem.getPageClass());
					link.add(new Label("adminLinkLabel", naviItem.getPageName()));
					item.add(link);
				}
			};
			
			add(naviView);
		}
	}

	/**
	 * A panel that is only visible for users with role ADMIN or USER.
	 */
	@AuthorizeAction(action = Action.RENDER, roles = { Roles.ADMIN, Roles.USER })
	private static final class ForAdminsAndUsers extends Panel {
		/**
		 * Construct.
		 * 
		 * @param id
		 */
		public ForAdminsAndUsers(String id, final BodyFrame index) {
			super(id);
			BookmarkablePageLink<?> link = new BookmarkablePageLink<Void>("linkToLogout", LogoutPage.class);
			add(link);

			// generate data-view to populate the table
			DataView<NavigationItem> naviView = new DataView<NavigationItem>("userNavis",
					new ListDataProvider<NavigationItem>(index.getUserNavigationList())) {

				@Override
				protected void populateItem(Item<NavigationItem> item) {
					NavigationItem naviItem = item.getModelObject();

					// link to the page
					ChangeFramePageLink link = new ChangeFramePageLink("linkToUserPage", index, naviItem.getPageClass());
					link.add(new Label("userLinkLabel", naviItem.getPageName()));
					item.add(link);
				}
			};
			
			add(naviView);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param index
	 *            parent frame class
	 */
	public NavigationFrame(BodyFrame index) {
		add(new ForAdminsAndUsers("forAdminsAndUsersPanel", index));
		add(new ForAdmins("forAdminsPanel", index));
	}

	protected void refresh() {
		this.renderPage();
	}

	/**
	 * No need for versioning this frame.
	 * 
	 * @see org.apache.wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned() {
		return false;
	}
}