package processor;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.util.ArraySet;
import edu.stanford.nlp.util.CoreMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * annotate the sentence in the document with type.
 * this annotation is used to classify the sentences in document into different type,
 * the type is from {Maalej W., Robillard M. P. Patterns of Knowledge in API Reference Documentation. IEEE Transactions on
 * Software Engineering, 2013, 39(9): 1264-82.}
 * current is only focus on FUNCTION,DIRECTIVES,OTHERS two type.
 */
public class SentenceKnowledgePatterAnnotator implements Annotator {
    public SentenceKnowledgePatterAnnotator() {
        //todo can load model from here
    }

    @Override
    public void annotate(Annotation annotation) {
        //todo complete this sentence classify
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        CoreMap firstSentence = sentences.get(0);
        firstSentence.set(SentenceKnowledgePatternAnnotation.class, SentenceKnowledgePatternAnnotation.FUNCTION);

        for (int i = 1; i < sentences.size(); i++) {
            // todo complete th
            sentences.get(i).set(SentenceKnowledgePatternAnnotation.class, SentenceKnowledgePatternAnnotation.DIRECTIVES);
        }
    }


    @Override
    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
        return Collections.singleton(SentenceKnowledgePatternAnnotation.class);
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requires() {
        return Collections.unmodifiableSet(new ArraySet<>(Arrays.asList(
                CoreAnnotations.SentencesAnnotation.class
        )));
    }
}
