package de.tu_berlin.cit.intercloud.client.exception;

public abstract class IntercloudClientException extends Exception {
    public IntercloudClientException(String message) {
        super(message);
    }

    public IntercloudClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
