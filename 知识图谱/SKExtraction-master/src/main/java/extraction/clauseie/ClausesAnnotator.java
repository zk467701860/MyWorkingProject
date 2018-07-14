package extraction.clauseie;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.util.CoreMap;
import extraction.RelationExtractionAnnotator;
import extraction.function.FunctionRelationAnnotation;
import extraction.schema.entity.TextEntity;
import extraction.schema.relation.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * the annotator to detect the clauses in the sentence
 */
public class ClausesAnnotator implements Annotator {
    @Override
    public void annotate(Annotation annotation) {
        //todo complete complete the relation extract method
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        List<Relation> relationList = new ArrayList<>();
        relationList.add(new Relation(new TextEntity("API1"),"has function",new TextEntity("parse XML")));

        for (CoreMap coreMap : sentences) {
            //todo analysis the sentence and and set in the relation
            coreMap.set(FunctionRelationAnnotation.class, relationList);
        }
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
        return null;
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requires() {
        return null;
    }
}
