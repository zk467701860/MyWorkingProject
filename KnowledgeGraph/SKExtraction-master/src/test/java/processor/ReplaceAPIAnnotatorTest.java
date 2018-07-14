package processor;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.PropertiesUtils;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class ReplaceAPIAnnotatorTest {
    @Test
    void annotate() {
       String sentence= "android.view.Window is a class";
        Properties props = PropertiesUtils.asProperties(
                // "annotators", "tokenize,ssplit,pos,lemma,parse,ner,dcoref,depparse",
                //"annotators", "tokenize,ssplit,pos,lemma,parse",
                "annotators", "custom.api.replace,tokenize,ssplit,pos",//depparse is based on neural network
                "parse.keepPunct", "false",
                "tokenize.language", "en",
                "customAnnotatorClass.custom.api.replace","processor.ReplaceAPIAnnotator");
        StanfordPipelineUtil stanfordPipelineUtil=new StanfordPipelineUtil(props);
        Annotation annotation=stanfordPipelineUtil.annotate(sentence);
        System.out.println("replace:"+annotation.toString());

        System.out.println("original:"+ReplaceAPIAnnotator.recoverText(annotation));
    }

    @Test
    void getTheOriginalText() {
    }

}