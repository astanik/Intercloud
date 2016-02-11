package de.tu_berlin.cit.intercloud.client.model.rest.method;

public class TextRepresentationModel implements IRepresentationModel {
    private static final long serialVersionUID = 1117224125569444798L;

    private String text;

    public TextRepresentationModel(String text) {
        this.text = text;
    }

    public TextRepresentationModel() {
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
