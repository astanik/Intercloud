package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.time.Duration;
import java.util.Locale;

/**
 * Converts a {@link Duration} into a {@link String} and vise versa.
 */
public class DurationConverter implements IConverter<Duration> {

    @Override
    public Duration convertToObject(String s, Locale locale) throws ConversionException {
        try {
            return s != null ? Duration.parse(s) : null;
        } catch (Exception e) {
            throw new ConversionException("'" + s + "' is not a valid Duration.");
        }
    }

    @Override
    public String convertToString(Duration duration, Locale locale) {
        return null != duration ? duration.toString() : null;
    }
}
