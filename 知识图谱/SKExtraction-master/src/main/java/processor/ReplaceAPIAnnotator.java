package processor;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.util.ArraySet;
import edu.stanford.nlp.util.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * a annotator to replace the API in the text with API Placeholder,reduce the code element effect to
 * following natural language process.
 * original text:
 * "android.view.Window is a top-level view class."
 * after annotate :
 * "API1 is a top-level view class."
 * ("API1","android.view.Window") will be store in the map in ReplaceAPIAnnotation.class
 */

public class ReplaceAPIAnnotator implements Annotator {

    public void annotate(Annotation annotation) {
        //todo complete this method to replace the api with the
        String textAnnotation = annotation.get(CoreAnnotations.TextAnnotation.class);
        HashMap<String, String> replaceMap = new HashMap<>();
        if (textAnnotation.contains("android.view.Window")) {
            textAnnotation = textAnnotation.replace("android.view.Window", "API1");
            Pair<String, String> stringPair = Pair.makePair("API1", "android.view.Window");
            annotation.set(CoreAnnotations.TextAnnotation.class, textAnnotation);

            replaceMap.put("API1", "android.view.Window");
            annotation.set(ReplaceAPIAnnotation.class, replaceMap);
        }
                /*
        for (CoreLabel token : annotation.get(CoreAnnotations.TokensAnnotation.class)) {
            String lemma = wordToLemma.getOrDefault(token.word(), token.word());
            token.set(CoreAnnotations.LemmaAnnotation.class, lemma);
        }*/
    }

    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
        return Collections.singleton(ReplaceAPIAnnotation.class);
    }

    public Set<Class<? extends CoreAnnotation>> requires() {
        /*
        return Collections.unmodifiableSet(new ArraySet<>(Collections.singletonList(
                CoreAnnotations.TextAnnotation.class
        )));
       */
        return Collections.unmodifiableSet(new ArraySet<>());
    }

    /**
     * get the original text from the a annotated text, recover the API actual name from the text
     * example: "java.util.HashMap is a class."->"API1 is a class."
     * this is done by this ReplaceAPIAnnotator class.
     * <p>
     * when you get a Annotation which is done by ReplaceAPIAnnotator,
     * you want to get the original text before replace. you can call this method.
     *
     * @param annotation the annotated text,
     * @return the original text
     */
    public static String recoverText(Annotation annotation) {
        String originalString = annotation.get(CoreAnnotations.TextAnnotation.class);
        if (annotation.containsKey(ReplaceAPIAnnotation.class)) {
            HashMap<String, String> replaceMap = annotation.get(ReplaceAPIAnnotation.class);

            if (replaceMap != null) {
                for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
                    originalString = originalString.replace(entry.getKey(), entry.getValue());
                }
            }
        }
        return originalString;
    }
}
