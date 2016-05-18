package de.tu_berlin.cit.intercloud.webapp.panels.browser;

import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.Page;

import java.io.Serializable;

/**
 * Specifies the response page of a resource path element
 * displayed as a link in the {@link BreadcrumbPanel}.
 */
public interface IBreadcrumbRedirect extends Serializable {
    Page getResponsePage(XmppURI uri);
}
