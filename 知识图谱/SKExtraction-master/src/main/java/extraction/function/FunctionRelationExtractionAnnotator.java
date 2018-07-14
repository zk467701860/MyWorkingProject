package extraction.function;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import extraction.RelationExtractionAnnotator;
import extraction.schema.entity.TextEntity;
import extraction.schema.relation.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * the relation extractor for
 */
public class FunctionRelationExtractionAnnotator extends RelationExtractionAnnotator {
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
}
