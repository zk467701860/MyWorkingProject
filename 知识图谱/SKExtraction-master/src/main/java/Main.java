import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import extraction.constraint.ConstraintRelationAnnotation;
import extraction.function.FunctionRelationAnnotation;
import extraction.schema.APIDataSource;
import extraction.schema.APIDocument;
import extraction.schema.APIText;
import extraction.schema.relation.Relation;
import io.RawDataLoader;
import processor.SentenceCompleteAnnotation;
import processor.SentenceKnowledgePatternAnnotation;
import processor.StanfordPipelineFactory;
import processor.StanfordPipelineUtil;

import java.io.File;

public class Main {
    public static void main(String args[]) {
        //String filePath = File("src/main/resources/data/raw_document_android.view.Window.txt").getFile();
        String filePath = new File("src/main/resources/data/raw_document_android.view.Window.txt").getAbsolutePath();
        //当前类的绝对路径
        System.out.println(filePath);

        APIDataSource apiDataSource = RawDataLoader.loadFromTXT(filePath);
        StanfordPipelineUtil relationExtractionPipeline = StanfordPipelineFactory.getRelationExtractionPipeline();
        for (APIDocument apiDocument : apiDataSource.getAPIDocumentList()) {
            for (APIText apiText : apiDocument.getApiTextList()) {
                Annotation annotation = relationExtractionPipeline.annotate(apiText.parseToAnnotation());
                for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                    System.out.println("sentence : " + sentence.get(CoreAnnotations.TextAnnotation.class));

                    System.out.println("sentence type : " + sentence.get(SentenceKnowledgePatternAnnotation.class));
                    System.out.println("constraint relation : ");
                    for (Relation relation : sentence.get(ConstraintRelationAnnotation.class)) {
                        System.out.println(relation.toString());
                    }
                    System.out.println("function relation : ");
                    for (Relation relation : sentence.get(FunctionRelationAnnotation.class)) {
                        System.out.println(relation.toString());
                    }
                }
                System.out.println("text before complete: " + annotation.get(SentenceCompleteAnnotation.class));
            }
        }
    }
}
