package processor;

import edu.stanford.nlp.ling.CoreAnnotation;
import io.APITextProperties;

public class APITextExtraInfoAnnotation implements CoreAnnotation<APITextProperties> {

    @SuppressWarnings("unchecked")
    public Class<APITextProperties> getType() {
        return APITextProperties.class;
    }
}
