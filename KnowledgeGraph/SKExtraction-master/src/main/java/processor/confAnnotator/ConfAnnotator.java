package processor.confAnnotator;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;


public class ConfAnnotator {
    public void setStr(String str) {
        this.str = str;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getStr() {
        return str;
    }

    private String str;
    private String n;

    public void setClass_n(String class_n) {
        this.class_n = class_n;
    }

    private String class_n;

    public ConfAnnotator(){
    }
    /**
     * replace some wrong words
     */
    public void replaceWord(){
        if (str.equals("") || str == null) {
            return;
        }

        Boolean in = false;

        // name (an a the this)+classname iswhen iswhat iswhere iswhile isif
        if (str.contains(n + " is " + n)) {
            setStr(str.replace(n + " is " + n, n));
            in = true;
        } else if (str.contains(n + " is When")) {
            setStr(str.replace(n + " is When", "When"));
            in = true;
        } else if (str.contains(n + " is What")) {
            setStr(str.replace(n + " is What", "What"));
            in = true;
        } else if (str.contains(n + " is Where")) {
            setStr(str.replace(n + " is Where", "Where"));
            in = true;
        } else if (str.contains(n + " is While")) {
            setStr(str.replace(n + " is While", "While"));
            in = true;
        } else if (str.contains(n + " is If")) {
            setStr(str.replace(n + " is If", "If"));
            in = true;
        }

        if (in) {

        }
    }

    //-------------change the conf to none
    public void changeConf(Annotation document) {
        String origin = "";
        for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
            origin = cc.getMentionsInTextualOrder().get(0).mentionSpan;

            for (int i = 1; i < cc.getMentionsInTextualOrder().size(); i++) {
                String replace = cc.getMentionsInTextualOrder().get(i).mentionSpan;
                str = str.replaceFirst(replace, origin);
            }
            //System.out.println(str);
        }
        /*for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("---");
            System.out.println("mentions");
            for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
                System.out.println("\t" + m);
            }
        }*/
    }

    public String changeToTrueName(int num) {//-------- 0 package   1 class 2 method  3 parameter
        switch (num) {
            case 0:
                str = str.replace("This Package", n);
                str = str.replace("This package", n);
                str = str.replace("this Package", n);
                str = str.replace("this package", n);
            case 1:
                str = str.replace("This Class", n);
                str = str.replace("this Class", n);
                str = str.replace("this class", n);
                str = str.replace("This class", n);
                str = str.replace("This Interface", n);
                str = str.replace("This interface", n);
                str = str.replace("this Interface", n);
                str = str.replace("this interface", n);
            case 2:
                str = str.replace("This Package", n);
                str = str.replace("This package", n);
                str = str.replace("this Package", n);
                str = str.replace("this package", n);
        }

        return str;
    }

    /**
     * get first world type
     * 0 verb    1 none
     */
    public void getFirst(Annotation document) {
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            SemanticGraph relation = sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class);
            IndexedWord word1 = relation.getNodeByIndex(1);
            IndexedWord word2 = relation.getNodeByIndex(2);

            if ((word1.get(CoreAnnotations.TextAnnotation.class) + word2.get(CoreAnnotations.TextAnnotation.class) + relation.getNodeByIndex(3).get(CoreAnnotations.TextAnnotation.class) + relation.getNodeByIndex(4).get(CoreAnnotations.TextAnnotation.class)).contains(class_n)) {
                return;
            }

            SemanticGraphEdge edge = relation.getEdge(word2, word1);
            SemanticGraphEdge edge1 = relation.getEdge(word1, word2);

            String relationName = "";
            if (edge != null) {
                relationName = edge.getRelation().getShortName();
            } else if (edge1 != null) {
                relationName = edge1.getRelation().getShortName();
            }

            if (!"".equals(relationName)) {
                if ("NN NNS NNP NNPS".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class)) && "NN NNS NNP NNPS".contains(word2.get(CoreAnnotations.PartOfSpeechAnnotation.class)) && "compound".equals(relationName)) {
                    //----组合名词
                    setStr(n + " is " + str);
                } else if ("DT".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class)) && "NN NNS NNP NNPS".contains(word2.get(CoreAnnotations.PartOfSpeechAnnotation.class)) && "det".equals(relationName)) {
                    //----限定词名词
                    setStr(n + " is " + str);
                } else if ("VB VBD VBG VBN VBP VBZ".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class))) {
                    setStr(n + " " + str);
                }
            } else {
                if ("NN NNS NNP NNPS".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class))) {
                    //----组合名词
                    setStr(n + " is " + str);
                } else if ("VB VBD VBG VBN VBP VBZ".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class))) {
                    setStr(n + " " + str);
                }
            }

            System.out.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.LIST));
            System.out.println("--------SemanticGraph EnhancedDependencies READABLE-format--------");
            System.out.println(sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.READABLE));
            return;
        }
    }

    /**
     * only deal with one sentence in a time
     *
     * @param sentence
     * @return
     */
    public String getFirstForSentence(CoreMap sentence) {
        String sentenceText = sentence.get(CoreAnnotations.TextAnnotation.class);
        SemanticGraph relation = sentence.get(SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation.class);
        IndexedWord word1 = relation.getNodeByIndex(1);
        IndexedWord word2 = relation.getNodeByIndex(2);

        if ((word1.get(CoreAnnotations.TextAnnotation.class) + word2.get(CoreAnnotations.TextAnnotation.class) + relation.getNodeByIndex(3).get(CoreAnnotations.TextAnnotation.class) + relation.getNodeByIndex(4).get(CoreAnnotations.TextAnnotation.class)).contains(class_n)) {
            return sentenceText;
        }

        SemanticGraphEdge edge = relation.getEdge(word2, word1);
        SemanticGraphEdge edge1 = relation.getEdge(word1, word2);

        String relationName = "";
        if (edge != null) {
            relationName = edge.getRelation().getShortName();
        } else if (edge1 != null) {
            relationName = edge1.getRelation().getShortName();
        }

        if (!"".equals(relationName)) {
            if ("NN NNS NNP NNPS".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class)) && "NN NNS NNP NNPS".contains(word2.get(CoreAnnotations.PartOfSpeechAnnotation.class)) && "compound".equals(relationName)) {
                //----组合名词
                return n + " is " + sentenceText;
            } else if ("DT".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class)) && "NN NNS NNP NNPS".contains(word2.get(CoreAnnotations.PartOfSpeechAnnotation.class)) && "det".equals(relationName)) {
                //----限定词名词
                return n + " is " + str;
            } else if ("VB VBD VBG VBN VBP VBZ".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class))) {
                return n + " " + str;
            }
        } else {
            if ("NN NNS NNP NNPS".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class))) {
                //----组合名词
                return n + " is " + str;
            } else if ("VB VBD VBG VBN VBP VBZ".contains(word1.get(CoreAnnotations.PartOfSpeechAnnotation.class))) {
                return n + " " + str;
            }
        }
        return sentenceText;
    }
}