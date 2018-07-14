package processor;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.util.ErasureUtils;

import java.util.HashMap;

public class ReplaceAPIAnnotation implements CoreAnnotation<HashMap<String,String>> {

    @SuppressWarnings("unchecked")
    public Class<HashMap<String,String>> getType() {
        return ErasureUtils.uncheckedCast(HashMap.class);
    }
}
