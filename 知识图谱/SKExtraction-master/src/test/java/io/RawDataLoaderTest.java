package io;

import extraction.schema.APIDataSource;
import extraction.schema.APIDocument;
import org.junit.jupiter.api.Test;

class RawDataLoaderTest {
    @Test
    void loadFromTXT() {
        String filePath = RawDataLoader.class.getResource("/data/raw_document_android.view.Window.txt").getFile();
        //当前类的绝对路径
        System.out.println(filePath);

        APIDataSource apiDataSource = RawDataLoader.loadFromTXT(filePath);
        for(APIDocument apiDocument:apiDataSource.getAPIDocumentList())
            System.out.println(apiDocument.toString());
    }

}