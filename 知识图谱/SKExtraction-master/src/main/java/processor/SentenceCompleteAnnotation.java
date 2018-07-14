package processor;

import edu.stanford.nlp.ling.CoreAnnotation;

/**
 * the annotation for subject complete,store the old sentence before complete.
 */
public class SentenceCompleteAnnotation implements CoreAnnotation<String> {

    @SuppressWarnings("unchecked")
    public Class<String> getType() {
        return String.class;
    }
}
