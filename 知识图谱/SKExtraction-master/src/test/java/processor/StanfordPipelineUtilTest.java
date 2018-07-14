package processor;

import edu.stanford.nlp.pipeline.Annotation;

class StanfordPipelineUtilTest {
    private StanfordPipelineUtil stanfordPipelineUtil;
    private StanfordPipelineUtil continuePipeline;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        stanfordPipelineUtil = StanfordPipelineFactory.getDefaultNLPPipeline();
        continuePipeline = StanfordPipelineFactory.getSimpleNLPPipeline();

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void annotate() {
        String text = "Abstract base class for a top-level window look and behavior policy. An instance of this class should be used as the top-level view added to the window manager. It provides standard UI policies such as a background, title area, default key processing, etc.";
        Annotation annotation = stanfordPipelineUtil.annotate(text);
        System.out.println("Annotation:" + annotation.toString());

        annotation=continuePipeline.annotate(annotation);
        System.out.println("Annotation:" + annotation.toString());

    }

}