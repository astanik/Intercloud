package de.tu_berlin.cit.intercloud.webapp;

public class XwadlFileConfig {
    private boolean hasKind;
    private int numOfKindMixins;
    private int numOfLinks;
    private int numOfLinkMixins;
    private int numOfCategoryMixins;
    private int numOfTemplates;
    private boolean hasDefaultValues;

    public XwadlFileConfig() {
    }

    public XwadlFileConfig(boolean hasKind, int numOfKindMixins,
                           int numOfLinks, int numOfLinkMixins,
                           int numOfCategoryMixins,
                           int numOfTemplates,
                           boolean hasDefaultValues) {
        this.hasKind = hasKind;
        this.numOfKindMixins = numOfKindMixins;
        this.numOfLinks = numOfLinks;
        this.numOfLinkMixins = numOfLinkMixins;
        this.numOfCategoryMixins = numOfCategoryMixins;
        this.numOfTemplates = numOfTemplates;
        this.hasDefaultValues = hasDefaultValues;
    }

    public boolean hasKind() {
        return hasKind;
    }

    public void setHasKind(boolean hasKind) {
        this.hasKind = hasKind;
    }

    public int getNumOfKindMixins() {
        return numOfKindMixins;
    }

    public void setNumOfKindMixins(int numOfKindMixins) {
        this.numOfKindMixins = numOfKindMixins;
    }

    public int getNumOfLinks() {
        return numOfLinks;
    }

    public void setNumOfLinks(int numOfLinks) {
        this.numOfLinks = numOfLinks;
    }

    public int getNumOfLinkMixins() {
        return numOfLinkMixins;
    }

    public void setNumOfLinkMixins(int numOfLinkMixins) {
        this.numOfLinkMixins = numOfLinkMixins;
    }

    public int getNumOfCategoryMixins() {
        return numOfCategoryMixins;
    }

    public void setNumOfCategoryMixins(int numOfCategoryMixins) {
        this.numOfCategoryMixins = numOfCategoryMixins;
    }

    public int getNumOfTemplates() {
        return numOfTemplates;
    }

    public void setNumOfTemplates(int numOfTemplates) {
        this.numOfTemplates = numOfTemplates;
    }

    public boolean hasDefaultValues() {
        return hasDefaultValues;
    }

    public void setHasDefaultValues(boolean hasDefaultValues) {
        this.hasDefaultValues = hasDefaultValues;
    }
}
