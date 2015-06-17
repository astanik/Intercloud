package de.tu_berlin.cit.intercloud.xmpp.rest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface XmppMethod {

    /**
     * XMPP GET method.
     */
    public static final String GET = "GET";
    /**
     * XMPP POST method.
     */
    public static final String POST = "POST";
    /**
     * XMPP PUT method.
     */
    public static final String PUT = "PUT";
    /**
     * XMPP DELETE method.
     */
    public static final String DELETE = "DELETE";

    /**
     * Specifies the name of a XMPP method. E.g. "GET".
     */
    String value();

}
