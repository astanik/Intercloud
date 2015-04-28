package de.tu_berlin.cit.intercloud.root.services;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

@Singleton
public class TestSingleton {

	private String name;
	
	@PostConstruct
    private void init() {
        name="Robin Hood";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
