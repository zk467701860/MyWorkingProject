package io;

public class APITextPropertiesUtil {
    public static final String KEY_SPLIT = "#:";
    public static final String KEY_VALUE_SPLIT = "%%";

    public static APITextProperties parseFromString(String text) {
        String[] splitWords = text.split(KEY_SPLIT);
        APITextProperties properties = new APITextProperties();
        if (splitWords.length == 1) {
            properties.setProperty(APITextProperties.TEXT, splitWords[0]);
            return properties;
        } else {
            for (String propertyToValuePair :
                    splitWords) {
                String[] keyValuePair = propertyToValuePair.split(KEY_VALUE_SPLIT, 2);
                if (keyValuePair.length >= 2) {
                    properties.setProperty(keyValuePair[0], keyValuePair[1]);
                }
            }
            return properties;
        }
    }
}
