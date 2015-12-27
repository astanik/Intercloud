package de.tu_berlin.cit.intercloud.client.exception;

public class ParameterFormatException extends IntercloudClientException {
    public ParameterFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterFormatException(String message) {
        super(message);
    }
}
