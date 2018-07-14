package extraction;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArraySet;
import edu.stanford.nlp.util.CoreMap;
import extraction.schema.relation.Relation;

import java.util.*;

/**
 * an annotator that extract the relation triple
 */
public class RelationExtractionAnnotator implements Annotator {
    public RelationExtractionAnnotator() {
        //todo can load model from here
    }

    @Override
    public void annotate(Annotation annotation) {
        //todo complete complete the relation extract method
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        List<Relation> relationList=new ArrayList<>();

        for(CoreMap coreMap:sentences){
            coreMap.set(RelationAnnotation.class,relationList);
        }
    }


    @Override
    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
        return Collections.singleton(RelationAnnotation.class);
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requires() {
        return Collections.unmodifiableSet(new ArraySet<>(Arrays.asList(
                CoreAnnotations.TokensAnnotation.class,
                CoreAnnotations.SentencesAnnotation.class,
                CoreAnnotations.PartOfSpeechAnnotation.class,
                CoreAnnotations.LemmaAnnotation.class,
                TreeCoreAnnotations.TreeAnnotation.class,
                SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class
        )));
    }
}
