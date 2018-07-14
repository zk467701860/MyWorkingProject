package io;

import java.util.Properties;

/**
 * the API text property name
 */
public class APITextProperties extends Properties {
    public static final String API_NAME = "API_name";
    public static final String PARENT_API_NAME = "parent_API_name";
    public static final String TEXT_TITLE = "text_title";
    public static final String SUBTITLE = "sub_title";
    public static final String SUBTITLE_INDEX = "sub_title_index";
    public static final String TEXT = "text";
    public static final String SENTENCE_INDEX = "sentence_index";
    public static final String PARAGRAPH_INDEX = "paragraph_index";
    public static final String DOCUMENT_INDEX = "document_index";
    public static final String KNOWLEDGE_PATTERN = "knowledge_pattern";

    public static final String UNKNOWN_API = "UNKNOWN_API";

    public String getApiName() {
        return getProperty(API_NAME, UNKNOWN_API);
    }

    public String getText() {
        return getProperty(TEXT, "");
    }
}
