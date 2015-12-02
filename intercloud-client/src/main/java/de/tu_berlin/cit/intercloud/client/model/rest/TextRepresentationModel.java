package de.tu_berlin.cit.intercloud.client.model.rest;

public class TextRepresentationModel extends AbstractRepresentationModel {
    private String text;

    public TextRepresentationModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
