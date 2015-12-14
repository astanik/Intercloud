package de.tu_berlin.cit.intercloud.client.exception;

public class AttributeFormatException extends IntercloudClientException {
    public AttributeFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttributeFormatException(String message) {
        super(message);
    }
}
