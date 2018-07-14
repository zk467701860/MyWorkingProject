package io;

import extraction.schema.APIText;

public class APITextReader {
    private String textBuffer = "";

    public void addTextToBuffer(String text) {
        this.textBuffer = this.textBuffer + text;
    }

    public void setTextBuffer(String textBuffer) {
        this.textBuffer = textBuffer;
    }

    public APIText parse() {
        APITextProperties properties = APITextPropertiesUtil.parseFromString(this.textBuffer);
        return new APIText(properties);
    }

    public boolean isEmpty() {
        if (textBuffer == null || "".equals(textBuffer)) {
            return true;
        } else
            return false;
    }

    public void clear() {
        this.textBuffer = "";
    }
}
