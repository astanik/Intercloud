package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Converts a {@link List<String>} into a {@link String} and vise versa.
 * The {@link String}'s format is similar to {@link de.tu_berlin.cit.intercloud.occi.core.xml.representation.LinkType}.
 */
public class ListConverter implements IConverter<List<String>> {
    public static final String LIST_SEPARATOR = ";";
    public static final String LIST_SPLIT_PATTERN = "\\s*" + LIST_SEPARATOR + "\\s*"; // split and trim

    @Override
    public List<String> convertToObject(String s, Locale locale) throws ConversionException {
        return null != s ? Arrays.asList(s.trim().split(LIST_SPLIT_PATTERN)) : null;
    }

    @Override
    public String convertToString(List<String> strings, Locale locale) {
        return null != strings ? String.join(LIST_SEPARATOR + "\n", strings) : null;
    }
}
