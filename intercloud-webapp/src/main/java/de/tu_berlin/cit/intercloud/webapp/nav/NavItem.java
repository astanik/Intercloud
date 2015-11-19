package de.tu_berlin.cit.intercloud.webapp.nav;

import org.apache.wicket.Page;

public class NavItem {
    private String label;
    private Class<? extends Page> page;

    public NavItem(String label, Class<? extends Page> page) {
        this.label = label;
        this.page = page;
    }

    public String getLabel() {
        return label;
    }

    public Class<? extends Page> getPage() {
        return page;
    }
}
