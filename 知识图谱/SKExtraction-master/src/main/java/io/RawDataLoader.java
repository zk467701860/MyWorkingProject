package io;

import com.sun.istack.internal.NotNull;
import extraction.schema.APIDataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * load the raw data file for API relation extraction, from txt file
 */
public class RawDataLoader {
    public RawDataLoader() {
    }

    public static APIDataSource loadFromTXT(@NotNull String filePath) {
        APIDataSource apiDataSource = new APIDataSource();

        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            System.out.println("load the data from " + filePath);
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            int line = 1;
            APITextReader apiTextReader = new APITextReader();
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                if (tempString.contains(APITextPropertiesUtil.KEY_SPLIT)) {
                    if (!apiTextReader.isEmpty()) {
                        apiDataSource.addAPIText(apiTextReader.parse());
                        apiTextReader.clear();
                    }
                }
                apiTextReader.addTextToBuffer(tempString);
                line++;
            }
            if (!apiTextReader.isEmpty()) {
                apiDataSource.addAPIText(apiTextReader.parse());
                apiTextReader.clear();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return apiDataSource;
    }

}
