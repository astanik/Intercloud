/**
 * Copyright 2010-2015 Complex and Distributed IT Systems, TU Berlin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tu_berlin.cit.intercloud.webapp;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.tu_berlin.cit.intercloud.client.profiling.impl.LoggerListener;
import de.tu_berlin.cit.intercloud.client.profiling.impl.ProfilingService;
import de.tu_berlin.cit.intercloud.webapp.pages.LoginPage;
import de.tu_berlin.cit.intercloud.webapp.pages.WelcomePage;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.util.time.Duration;

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

        // add your configuration here
        // put html pages into webapp/pages
        //getResourceSettings().getResourceFinders().add(new WebApplicationPath(getServletContext(), "pages"));
        initDevelopment();
        initBootstrap();
    }

    private void initDevelopment() {
        // reload classes for development
        if ("development".equalsIgnoreCase(getConfigurationType().name())) {
            getDebugSettings().setDevelopmentUtilitiesEnabled(true);
            getResourceSettings().setResourcePollFrequency(Duration.ONE_SECOND);
            getRequestCycleListeners().add(new ProfilingRequestCycleListener());
            //getRequestLoggerSettings().setRequestLoggerEnabled(true);
            getResourceSettings().setCachingStrategy(NoOpResourceCachingStrategy.INSTANCE);

            // Profiling with Logger
            ProfilingService profilingService = ProfilingService.getInstance();
            profilingService.setFilter(".*");
            profilingService.setListener(new LoggerListener());
        }
    }

    private void initBootstrap() {
        BootstrapSettings settings = new BootstrapSettings();

        //final ThemeProvider themeProvider = new BootswatchThemeProvider(BootswatchTheme.Spacelab);
        //settings.setThemeProvider(themeProvider);

        Bootstrap.install(this, settings);
    }

    @Override
    public Class<WelcomePage> getHomePage() {
        return WelcomePage.class;
    }

}
