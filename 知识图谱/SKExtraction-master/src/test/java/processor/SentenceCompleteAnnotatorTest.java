package processor;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class SentenceCompleteAnnotatorTest {
    @Test
    void annotate() {
        String sentence = "API_name%%DragEvent#:text%%Represents an event that is sent out by the system at various times during a drag and drop operation. It is a data structure that contains several important pieces of data about the operation and the underlying data.";
        Properties props = PropertiesUtils.asProperties(
                "annotators", "custom.extract.api.head,tokenize, ssplit, pos, lemma, parse, depparse, custom.sentence.complete",//depparse is based on neural network
                "parse.keepPunct", "false",
                "tokenize.language", "en",
                "customAnnotatorClass.custom.sentence.complete", "processor.SentenceCompleteAnnotator",
                "customAnnotatorClass.custom.extract.api.head", "processor.ExtractAPIExtraInfoAnnotator");
        StanfordPipelineUtil stanfordPipelineUtil = new StanfordPipelineUtil(props);
        Annotation annotation = stanfordPipelineUtil.annotate(sentence);
        System.out.println("whole doc text:" + annotation.toString());
        System.out.println("old sentences : " + annotation.get(SentenceCompleteAnnotation.class));

        for (CoreMap s : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("sentence : " + s.get(CoreAnnotations.TextAnnotation.class));
            System.out.println("--------SemanticGraph EnhancedDependencies LIST-format--------");
            System.out.println(s.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.LIST));

        }
    }

}