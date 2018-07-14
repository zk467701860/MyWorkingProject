package extraction.schema;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.PropertiesUtils;
import io.APITextProperties;
import processor.APITextExtraInfoAnnotation;

/**
 * a data wrapper for API data,contain the API name and its description
 */
public class APIText {
    private String APIName;
    private String APIText;
    private APITextProperties properties;

    public APIText(String APIName, String APIText) {
        this.APIName = APIName;
        this.APIText = APIText;
        properties = new APITextProperties();
        properties.setProperty(APITextProperties.API_NAME, APIName);
        properties.setProperty(APITextProperties.TEXT, APIText);
    }

    public APIText(String APIText) {
        this(APITextProperties.UNKNOWN_API, APIText);
    }


    public APIText(APITextProperties properties) {
        setProperties(properties);
    }

    public APITextProperties getProperties() {
        return properties;
    }

    public void setProperties(APITextProperties properties) {
        this.properties = properties;
        this.APIText = properties.getText();
        this.APIName = properties.getApiName();
    }

    public String getAPIText() {
        return APIText;
    }

    public void setAPIText(String APIText) {
        this.APIText = APIText;
    }

    public String getAPIName() {
        return APIName;
    }

    public void setAPIName(String APIName) {
        this.APIName = APIName;
    }

    public String toString() {
        return PropertiesUtils.asString(this.properties);
    }

    public String toFomatString() {
        return PropertiesUtils.asString(this.properties);
    }

    public int getDocumentIndex() {
        return PropertiesUtils.getInt(this.properties, APITextProperties.DOCUMENT_INDEX, APIDocument.DEFAULT_DOCUMENT_INDEX);
    }

    public Annotation parseToAnnotation() {
        Annotation annotation = new Annotation(this.getAPIText());
        annotation.set(APITextExtraInfoAnnotation.class, properties);
        return annotation;
    }
}
