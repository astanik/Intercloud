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

package de.tu_berlin.cit.intercloud.webapp;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;
import de.tu_berlin.cit.intercloud.webapp.template.LoginPage;
import de.tu_berlin.cit.intercloud.webapp.template.WelcomePage;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.util.file.WebApplicationPath;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

/**
 * Main class of the this web application.
 *
 * @author Alexander Stanik
 *
 */
public class IntercloudWebApplication extends AuthenticatedWebApplication {

	public IntercloudWebApplication() {
		super();
	}
	
    @Override
    public RuntimeConfigurationType getConfigurationType() {
    	return RuntimeConfigurationType.DEPLOYMENT;
    }

    @Override
    protected Class<IntercloudWebSession> getWebSessionClass() {
        return IntercloudWebSession.class;
    }

    @Override
    protected Class<LoginPage> getSignInPageClass() {
        return LoginPage.class;
//        return LoginPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new IntercloudWebSession(request);
    }
    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();
        getDebugSettings().setDevelopmentUtilitiesEnabled(true);

        getResourceSettings().getResourceFinders().add(new WebApplicationPath(getServletContext(), "pages"));
        // add your configuration here

        initBootstrap();
    }

    private void initBootstrap() {
        BootstrapSettings settings = new BootstrapSettings();

        final ThemeProvider themeProvider = new BootswatchThemeProvider(BootswatchTheme.Spacelab);
        settings.setThemeProvider(themeProvider);

        Bootstrap.install(this, settings);
    }

	@Override
	public Class<WelcomePage> getHomePage() {
		return WelcomePage.class;
	}

}
