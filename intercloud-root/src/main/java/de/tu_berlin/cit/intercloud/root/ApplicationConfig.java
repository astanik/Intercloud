package de.tu_berlin.cit.intercloud.root;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.tu_berlin.cit.intercloud.root.services.Test;

@ApplicationPath("resources")
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> rootResources = new HashSet<Class<?>>();
		rootResources.add(Test.class);
		return rootResources;
	}
	
}
