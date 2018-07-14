package processor;

import edu.stanford.nlp.util.PropertiesUtils;

import java.util.Properties;


public class StanfordPipelineFactory {
    public static StanfordPipelineUtil getDefaultNLPPipeline() {
        String annotators = "tokenize,ssplit,pos,lemma,parse,depparse";
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = PropertiesUtils.asProperties(
                "annotators", annotators,//depparse is based on neural network
                "parse.keepPunct", "false",
                "tokenize.language", "en");
        // build pipeline
        return new StanfordPipelineUtil(props);
    }
    public static StanfordPipelineUtil getSimpleNLPPipeline() {
        String annotators = "tokenize,ssplit,pos,lemma,parse,ner,depparse,mention,coref";
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = PropertiesUtils.asProperties(
                "annotators", annotators,//depparse is based on neural network
                "parse.keepPunct", "false",
                "tokenize.language", "en");
        // build pipeline
        return new StanfordPipelineUtil(props);
    }
    /**
     * build the pipeline for relation extraction for API document
     * @return the api document relation extraction pipeline
     */
    public static StanfordPipelineUtil getRelationExtractionPipeline(){
        Properties props = PropertiesUtils.asProperties(
                "annotators", "custom.extract.api.head,tokenize, ssplit, pos, lemma, parse, depparse, custom.sentence.complete, custom.sentence.knowledge.pattern, custom.re.function, custom.re.constraint",//depparse is based on neural network
                "parse.keepPunct", "false",
                "tokenize.language", "en",
                "customAnnotatorClass.custom.extract.api.head", "processor.ExtractAPIExtraInfoAnnotator",
                "customAnnotatorClass.custom.sentence.complete", "processor.SentenceCompleteAnnotator",
                "customAnnotatorClass.custom.sentence.knowledge.pattern", "processor.SentenceKnowledgePatterAnnotator",
                "customAnnotatorClass.custom.re.function", "extraction.function.FunctionRelationExtractionAnnotator",
                "customAnnotatorClass.custom.re.constraint", "extraction.constraint.ConstraintRelationExtractionAnnotator"

        );
        // build pipeline
        return new StanfordPipelineUtil(props);
    }

}
