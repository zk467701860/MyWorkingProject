package processor;

import edu.stanford.nlp.ling.CoreAnnotation;

/**
 * this annotation is used to classify the sentences in document into different type,
 * the type is from {Maalej W., Robillard M. P. Patterns of Knowledge in API Reference Documentation. IEEE Transactions on
 * Software Engineering, 2013, 39(9): 1264-82.}
 * current is only focus on FUNCTION,DIRECTIVES,OTHERS two type.
 */
public class SentenceKnowledgePatternAnnotation implements CoreAnnotation<String> {
    /*
    Functionality and Behavior,
    Describes what the APId oes( or does not do) in terms of functionality or features.
    Describes what happens when the API is used (a ﬁeld value is set, or a method is called).
     */
    public static final String FUNCTION = "Functionality and Behavior";
    /*
    Explains the meaning of terms used to name or describe an API element, or describes design or domain concepts used or implemented by the API.
     */
    public static final String CONCEPTS = "Concepts";
    /*
    Speciﬁes what users are allowed / not allowed to do with the API element. Directives are clear contracts.
     */
    public static final String DIRECTIVES = "Directives";
    /*
    the other type
     */
    public static final String OTHERS = "other";

    @SuppressWarnings("unchecked")
    public Class<String> getType() {
        return String.class;
    }
}
