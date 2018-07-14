package extraction.schema;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * a set of API text in the api document
 * for example, one page  in the whole api document can be organized as one APIDocument
 */
public class APIDocument {

    public static final int DEFAULT_DOCUMENT_INDEX = 0;
    private int documentIndex = DEFAULT_DOCUMENT_INDEX;
    private List<APIText> apiTextList;

    public APIDocument() {
        this(new ArrayList<>(), DEFAULT_DOCUMENT_INDEX);
    }

    public APIDocument(int documentIndex) {
        this(new ArrayList<>(), documentIndex);
    }

    public APIDocument(@NotNull List<APIText> apiTextList, int documentIndex) {
        this.documentIndex = documentIndex;
        this.apiTextList = apiTextList;
    }

    public List<APIText> getApiTextList() {
        return apiTextList;
    }

    public void setApiTextList(List<APIText> apiTextList) {
        this.apiTextList = apiTextList;
    }

    /**
     * add api text to this api document
     *
     * @param apiText the api text needed to add
     */
    public void add(APIText apiText) {
        this.apiTextList.add(apiText);
    }

    /**
     * add the api text to this api document
     *
     * @param apiName the api name
     * @param apiText the api text
     */
    public void add(String apiName, String apiText) {
        APIText api = new APIText(apiName, apiText);
        this.add(api);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (APIText apiText : apiTextList) {
            result.append("\n--------------\n").append(apiText.toString());
        }
        return result.toString();
    }

    public int getDocumentIndex() {
        return documentIndex;
    }

    public void setDocumentIndex(int documentIndex) {
        this.documentIndex = documentIndex;
    }
}
