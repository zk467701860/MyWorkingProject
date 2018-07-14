package processor;

import com.sun.istack.internal.NotNull;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.TypesafeMap;

import java.util.List;
import java.util.Properties;

public class StanfordPipelineUtil {
    private Properties props = null;
    private StanfordCoreNLP pipeline = null;
    private Class<? extends TypesafeMap.Key<SemanticGraph>> universalDependencyTypeClass = SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class;

    public StanfordPipelineUtil() {
        universalDependencyTypeClass = SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class;
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,lemma,parse,depparse",
                "parse.keepPunct", "false",
                "tokenize.language", "en");
        // build pipeline
        pipeline = new StanfordCoreNLP(props);
    }

    public StanfordPipelineUtil(@NotNull ProcessorOptions options) {
        String annotators = "tokenize,ssplit,pos,lemma,parse";
        if (options.neuralNetworkDependencyParser) {
            annotators = "tokenize,ssplit,pos,lemma,parse,depparse";
        }
        setUniversalDependencyTypeClass(options);
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        props = PropertiesUtils.asProperties(
                // "annotators", "tokenize,ssplit,pos,lemma,parse,ner,dcoref,depparse",
                //"annotators", "tokenize,ssplit,pos,lemma,parse",
                "annotators", annotators,//depparse is based on neural network
                "parse.keepPunct", "false",
                "tokenize.language", "en");
        // build pipeline
        pipeline = new StanfordCoreNLP(props);
    }

    public StanfordPipelineUtil(Properties props) {
        this.props = props;
        pipeline = new StanfordCoreNLP(props);
    }

    private void setUniversalDependencyTypeClass(@NotNull ProcessorOptions options) {
        if (ProcessorOptions.UNIVERSAL_DEPENDENCY_TYPE_BASIC.equals(options.UniversalDependencyType)) {
            universalDependencyTypeClass = SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class;
        }
        if (ProcessorOptions.UNIVERSAL_DEPENDENCY_TYPE_ENHANCED.equals(options.UniversalDependencyType)) {
            universalDependencyTypeClass = SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class;
        }
        if (ProcessorOptions.UNIVERSAL_DEPENDENCY_TYPE_ENHANCED_PLUSPLUS.equals(options.UniversalDependencyType)) {
            universalDependencyTypeClass = SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class;
        }
    }

    public Annotation annotate(String text) {
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);
        return document;
    }

    public Annotation annotate(Annotation document) {
        // run all Annotators on this text
        pipeline.annotate(document);
        return document;
    }

    public List<CoreMap> extractSentenceAnnotation(Annotation document) {
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        return document.get(CoreAnnotations.SentencesAnnotation.class);
    }

    public Tree extractParseTreeForSentence(CoreMap sentence) {
        // this is the parse tree of the current sentence
        return sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
    }

    public SemanticGraph extractDependencyGraphForSentence(CoreMap sentence) {
        // this is the dependency graph of the current sentence
        return sentence.get(universalDependencyTypeClass);
    }

}
