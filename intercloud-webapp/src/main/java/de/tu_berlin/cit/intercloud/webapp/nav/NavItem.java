package de.tu_berlin.cit.intercloud.webapp.nav;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;

public class NavItem {
    private String label;
    private Class<? extends Page> page;
    private boolean isActive;

    public NavItem(String label, Class<? extends Page> page, boolean isActive) {
        this.label = label;
        this.page = page;
        this.isActive = isActive;
    }

    public String getLabel() {
        return label;
    }

    public Class<? extends Page> getPage() {
        return page;
    }

    public boolean isActive() {
        return isActive;
    }

    public BookmarkablePageLink<Page> getLink(String id) {
        BookmarkablePageLink<Page> link = new BookmarkablePageLink<>(id, this.page);
        link.setBody(Model.of(label));
        return link;
    }
}
