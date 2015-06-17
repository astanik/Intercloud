package de.tu_berlin.cit.intercloud.xmpp.rest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Consumes {

	/**
	 * The media type that this method produces.
	 * @return media type
	 */
	String value();
	
	/**
	 * The class that is used for serialization.
	 * @return representation serializer
	 */
	Class<? extends Representation> serializer();
}
