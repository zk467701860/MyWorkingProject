package extraction.function;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import extraction.schema.relation.Relation;
import org.junit.jupiter.api.Test;
import processor.APITextExtraInfoAnnotation;
import processor.SentenceCompleteAnnotation;
import processor.SentenceKnowledgePatternAnnotation;
import processor.StanfordPipelineUtil;

import java.util.Properties;

class FunctionRelationExtractionAnnotatorTest {
    @Test
    void annotate() {
        String sentence = "android.view.Window is abstract base class for a top-level window look and behavior policy.";
        Properties props = PropertiesUtils.asProperties(
                "annotators", "custom.extract.api.head,tokenize, ssplit, pos, lemma, parse, depparse, custom.re.function",//depparse is based on neural network
                "parse.keepPunct", "false",
                "tokenize.language", "en",
                "customAnnotatorClass.custom.sentence.complete", "processor.SentenceCompleteAnnotator",
                "customAnnotatorClass.custom.extract.api.head", "processor.ExtractAPIExtraInfoAnnotator",
                "customAnnotatorClass.custom.sentence.knowledge.pattern", "processor.SentenceKnowledgePatterAnnotator",
                "customAnnotatorClass.custom.re.function", "extraction.function.FunctionRelationExtractionAnnotator"
        );
        StanfordPipelineUtil stanfordPipelineUtil = new StanfordPipelineUtil(props);
        Annotation annotation = stanfordPipelineUtil.annotate(sentence);
        System.out.println("whole doc text:" + annotation.toString());

        System.out.println("api properties:" + annotation.get(APITextExtraInfoAnnotation.class).toString());

        for (CoreMap s : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("sentence : " + s.get(CoreAnnotations.TextAnnotation.class));
            System.out.println("old sentence : " + s.get(SentenceCompleteAnnotation.class));

            System.out.println("--------SemanticGraph EnhancedDependencies LIST-format--------");
            System.out.println(s.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.LIST));
            System.out.println("sentence type : " + s.get(SentenceKnowledgePatternAnnotation.class));

            System.out.println("function relation : ");
            for (Relation relation : s.get(FunctionRelationAnnotation.class)) {
                System.out.println(relation.toString());
            }
        }
    }
}