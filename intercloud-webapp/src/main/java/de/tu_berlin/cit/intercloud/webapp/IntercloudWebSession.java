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

import de.tu_berlin.cit.intercloud.client.service.IIntercloudService;
import de.tu_berlin.cit.intercloud.client.service.impl.IntercloudService;
import de.tu_berlin.cit.intercloud.webapp.model.User;
import de.tu_berlin.cit.intercloud.xmpp.client.service.IXmppService;
import de.tu_berlin.cit.intercloud.xmpp.client.service.impl.XmppService;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntercloudWebSession extends AuthenticatedWebSession {
    private static final Logger logger = LoggerFactory.getLogger(IntercloudWebSession.class);

    private User user = null;
    // TODO: not transient
    private transient IXmppService xmppService;
    private transient IIntercloudService intercloudService;

    public IntercloudWebSession(Request request) {
        super(request);
    }

    @Override
    protected boolean authenticate(String username, String password) {
        try {
            User user = new User(username, Roles.USER);
            this.xmppService = new XmppService(user.getUri(), password);
            this.intercloudService = new IntercloudService(this.xmppService);
            this.user = user;
            return true;
        } catch (Exception e) {
            logger.error("Cannot connect to xmpp server. jid: {}", username, e);
            return false;
        }
    }

    public User getUser() {
        return user;
    }

    @Override
    public Roles getRoles() {
        if (isSignedIn() && user != null) {
            return user.getRoles();
        }
        return null;
    }

    @Override
    public void signOut() {
        super.signOut();
        if (null != this.xmppService) {
            this.xmppService.disconnect();
        }
        this.xmppService = null;
        this.intercloudService = null;
        this.user = null;
    }

    @Override
    public void onInvalidate() {
        // signout if session expires
        signOut();
    }

    public IXmppService getXmppService() {
        return xmppService;
    }

    public IIntercloudService getIntercloudService() {
        return intercloudService;
    }

    public static IntercloudWebSession get() {
        return (IntercloudWebSession) Session.get();
    }
}
