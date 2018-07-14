package processor;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.util.ArraySet;
import edu.stanford.nlp.util.PropertiesUtils;
import io.APITextProperties;
import io.APITextPropertiesUtil;

import java.util.Collections;
import java.util.Set;

/**
 * this annotator extract api text extra info from text,
 * defined in APITextProperties. and the README.md
 */
public class ExtractAPIExtraInfoAnnotator implements Annotator {

    /**
     * recover the from annotated text
     *
     * @param annotation the annotated text,
     * @return the original text
     */
    public static String recoverText(Annotation annotation) {
        if (annotation.containsKey(APITextExtraInfoAnnotation.class)) {
            return PropertiesUtils.asString(annotation.get(APITextExtraInfoAnnotation.class));
        } else
            return "APITextExtraInfoAnnotation not exist";
    }

    public void annotate(Annotation annotation) {
        String textAnnotation = annotation.get(CoreAnnotations.TextAnnotation.class);
        //the API extra info has been provided,we don't need to extract from txt
        //the text is promise to be clean
        if (annotation.containsKey(APITextExtraInfoAnnotation.class)) {

        } else {
            APITextProperties properties = APITextPropertiesUtil.parseFromString(textAnnotation);
            annotation.set(APITextExtraInfoAnnotation.class, properties);
            annotation.set(CoreAnnotations.TextAnnotation.class, properties.getText());
        }
    }

    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
        return Collections.singleton(APITextExtraInfoAnnotation.class);
    }

    public Set<Class<? extends CoreAnnotation>> requires() {
        return Collections.unmodifiableSet(new ArraySet<>());
    }
}
