package processor;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.PropertiesUtils;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class ExtractAPIExtraInfoAnnotatorTest {
    @Test
    void annotate() {
        String sentence = "API_name%%android.view.Window#:text%%android.view.Window is a class";
        Properties props = PropertiesUtils.asProperties(
                "annotators", "custom.extract.api.extra.info,tokenize,ssplit,pos",//depparse is based on neural network
                "parse.keepPunct", "false",
                "tokenize.language", "en",
                "customAnnotatorClass.custom.extract.api.extra.info", "processor.ExtractAPIExtraInfoAnnotator");
        StanfordPipelineUtil stanfordPipelineUtil = new StanfordPipelineUtil(props);
        Annotation annotation = stanfordPipelineUtil.annotate(sentence);
        System.out.println("replace:" + annotation.toString());

        System.out.println("original:" + ExtractAPIExtraInfoAnnotator.recoverText(annotation));
        System.out.println("api properties:" + annotation.get(APITextExtraInfoAnnotation.class).toString());

    }

}