package de.tu_berlin.cit.intercloud.webapp.components;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Converts a {@link Map<String, String>} into a {@link String} and vise versa.
 * The {@link String}'s format is similar to {@link de.tu_berlin.cit.intercloud.occi.core.xml.representation.MapType}.
 */
public class MapConverter implements IConverter<Map<String, String>> {
    public static final String KEY_VALUE_SEPARATOR = "=";
    public static final String KEY_VALUE_SPLIT_PATTERN = "\\s*" + KEY_VALUE_SEPARATOR + "\\s*"; // split and trim

    @Override
    public Map<String, String> convertToObject(String s, Locale locale) throws ConversionException {
        if (null == s) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String[] kvList = s.trim().split(ListConverter.LIST_SPLIT_PATTERN);
        for (String kvString : kvList) {
            String[] kv = kvString.split(KEY_VALUE_SPLIT_PATTERN);
            if (2 == kv.length) {
                map.put(kv[0], kv[1]);
            } else if (1 != kv.length || !kv[0].isEmpty()) { // skip ;;
                throw new ConversionException("'" + kvString + "' is not a valid Map entry.");
            }
        }
        return map;
    }

    @Override
    public String convertToString(Map<String, String> map, Locale locale) {
        if (null == map) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : entries) {
            if (!isFirst) {
                builder.append(ListConverter.LIST_SEPARATOR).append("\n");
            } else {
                isFirst = false;
            }
            builder.append(entry.getKey()).append(KEY_VALUE_SEPARATOR).append(entry.getValue());
        }
        return builder.toString();
    }
}
