package de.tu_berlin.cit.intercloud.xmpp.client.occi.representation;

import java.util.ArrayList;
import java.util.List;

public class Link extends Category {
    private String target;
    private List<Mixin> mixins = new ArrayList<>();

    public Link(String term, String schema) {
        super(term, schema);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void addMixin(Mixin mixin) {
        this.mixins.add(mixin);
    }

    public List<Mixin> getMixins() {
        return mixins;
    }
}
