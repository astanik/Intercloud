package de.tu_berlin.cit.intercloud.webapp.panels;

import de.tu_berlin.cit.rwx4j.XmppURI;
import org.apache.wicket.Page;

import java.io.Serializable;

public interface IBreadcrumbRedirect extends Serializable {
    Page getResponsePage(XmppURI uri);
}
