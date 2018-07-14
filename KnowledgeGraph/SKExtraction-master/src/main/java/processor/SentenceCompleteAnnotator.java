package processor;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArraySet;
import edu.stanford.nlp.util.CoreMap;
import processor.confAnnotator.ConfAnnotator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * complete the subject of the sentence,
 * it will need the full NLP analysis of the sentence,
 * after Complete the sentence,the tokenize and sentence
 * for example.
 * "Represents an event that is sent out by the system at various times during a drag and drop operation."
 * will be completed as
 * "DragEvent represents an event that is sent out by the system at various times during a drag and drop operation."
 * "DragEvent" will be provide by APITextExtraInfoAnnotation.
 */
public class SentenceCompleteAnnotator implements Annotator {
    /**
     * the nlp pipeline used for annotate the sentence again after it change
     */
    private StanfordPipelineUtil stanfordPipelineUtil;

    public SentenceCompleteAnnotator() {
        stanfordPipelineUtil = StanfordPipelineFactory.getDefaultNLPPipeline();
    }

    @Override
    public void annotate(Annotation annotation) {
        String APIName = annotation.get(APITextExtraInfoAnnotation.class).getApiName();

        //todo sentence complete need to complete in here
        ConfAnnotator confAnnotator_ = new ConfAnnotator();
        String oldDocumentText = annotation.get(CoreAnnotations.TextAnnotation.class);
        confAnnotator_.setStr(oldDocumentText);
        confAnnotator_.setN(APIName);
        confAnnotator_.setClass_n(APIName);
        confAnnotator_.changeToTrueName(0);
        confAnnotator_.getFirst(annotation);

        //confAnnotator_.changeConf());
        confAnnotator_.replaceWord();

        String newDocumentText = confAnnotator_.getStr();
        Annotation completeDocumentAnnotation = stanfordPipelineUtil.annotate(newDocumentText);
        annotation.set(SentenceCompleteAnnotation.class, oldDocumentText);
        annotation.set(CoreAnnotations.TextAnnotation.class, newDocumentText);

//todo 可以只取所有句子中的第一句，进行主语补全和重新标注（pos），然后再设置回Annotation里面
        //因为主语补全只影响第一句话，其他句子没有修改
        List<CoreMap> newCompleteSentenceAnnotation = completeDocumentAnnotation.get(CoreAnnotations.SentencesAnnotation.class);
        annotation.set(CoreAnnotations.SentencesAnnotation.class, newCompleteSentenceAnnotation);

    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
        return Collections.singleton(SentenceCompleteAnnotation.class);
    }

    @Override
    public Set<Class<? extends CoreAnnotation>> requires() {
        return Collections.unmodifiableSet(new ArraySet<>(Arrays.asList(
                CoreAnnotations.TokensAnnotation.class,
                CoreAnnotations.SentencesAnnotation.class,
                CoreAnnotations.PartOfSpeechAnnotation.class,
                CoreAnnotations.LemmaAnnotation.class,
                TreeCoreAnnotations.TreeAnnotation.class,
                APITextExtraInfoAnnotation.class,
                SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class
        )));
    }
}
