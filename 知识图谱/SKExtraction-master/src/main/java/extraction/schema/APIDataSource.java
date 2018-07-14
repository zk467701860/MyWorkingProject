package extraction.schema;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * a set of API document data source,contain some api document pages
 */
public class APIDataSource {
    private List<APIDocument> APIDocumentList;

    public APIDataSource() {
        this(new ArrayList<>());
    }

    public APIDataSource(@NotNull List<APIDocument> APIDocumentList) {
        this.APIDocumentList = APIDocumentList;
    }

    public APIDocument getAPIDocumentByDocumentIndex(int documentIndex) {
        for (APIDocument apiDocument :
                this.APIDocumentList) {
            if (apiDocument.getDocumentIndex() == documentIndex)
                return apiDocument;
        }
        return null;
    }

    public List<APIDocument> getAPIDocumentList() {
        return APIDocumentList;
    }

    public void addAPIText(@NotNull APIText apiText) {
        int documentIndex = apiText.getDocumentIndex();
        APIDocument apiDocument = getAPIDocumentByDocumentIndex(documentIndex);
        if (apiDocument == null) {
            apiDocument = new APIDocument(documentIndex);
            this.addAPIDocument(apiDocument);
        }
        apiDocument.add(apiText);
    }

    private void addAPIDocument(@NotNull APIDocument apiDocument) {
        this.APIDocumentList.add(apiDocument);
    }
}
