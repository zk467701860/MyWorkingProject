package extraction.constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;
import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import processor.StanfordPipelineUtil;

/**
 * extract the constraint from sentences
 */
public class ConstraintExtractor {
	StanfordPipelineUtil stanfordPipelineUtil;
    public ConstraintExtractor() {
    	stanfordPipelineUtil = new StanfordPipelineUtil();
    }
    public List<ConstraintEntity> extract(String text, String subject, String type) {
    	//针对异常的描述
    	Annotation annotation = null;
    	List<ConstraintEntity> constraintEntityList = null;
    	if (text.contains("(") && text.contains(")")) {
    		String sentence1 = text.substring(0,text.indexOf('(')) + text.substring(text.indexOf(')') + 1);
    		String sentence2 = text.substring(text.indexOf('(') + 1, text.indexOf(')'));
			System.out.println("split sentence1:  " + sentence1);
			System.out.println("split sentence2:  " + sentence2);
			annotation= stanfordPipelineUtil.annotate(sentence2);
			Tree tree = stanfordPipelineUtil.extractParseTreeForSentence(stanfordPipelineUtil.extractSentenceAnnotation(annotation).get(0));
			if (tree.toString().contains("(S") && sentence2.toLowerCase().contains("exception") && sentence2.toLowerCase().contains("throw")) {
				constraintEntityList = new ArrayList<ConstraintEntity>();
				constraintEntityList.addAll(extract(sentence2,subject,"exception"));
				constraintEntityList.addAll(extract(sentence1,subject,type));
				return constraintEntityList;
			}
		}
    	if(type.toLowerCase().contains("exception")) {
    		constraintEntityList = new ArrayList<ConstraintEntity>();
    		ConstraintEntity constraintEntity = new ConstraintEntity();
    		if(text.toLowerCase().contains("if")) {
    			constraintEntity.setType("if");
    			String precondition = "";
    			annotation= stanfordPipelineUtil.annotate(text);
    	        System.out.println("Annotation:"+annotation.toString());
    	        System.out.println("exception");
    	        int sentNo = 0;
    	        int beginIf = 0;
    	        String beforeWord = "";
    	        String currentWord = "";
    	        for(CoreMap sentence : stanfordPipelineUtil.extractSentenceAnnotation(annotation)){            
    	            System.out.println("Sentence #" + ++sentNo + ": " + sentence.get(CoreAnnotations.TextAnnotation.class)); 
    	            SemanticGraph dependencies = stanfordPipelineUtil.extractDependencyGraphForSentence(sentence);
    	        	//System.out.println(dependencies.toString(SemanticGraph.OutputFormat.LIST));
    	        	for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
    	        		if ((edge.getRelation().toString().equals("dobj") && edge.getSource().value().contains("throw")) || edge.getRelation().toString().equals("nsubjpass") && edge.getSource().value().contains("throw")) {
							System.out.println(edge.getTarget().value());
							constraintEntity.setObject(edge.getTarget().value());
							break;
						}
    	        		//System.out.println(edge.getRelation().toString());
    	        		//System.out.println(edge.getSource().toString(CoreLabel.OutputFormat.VALUE_INDEX));
    	        		//System.out.println(edge.getTarget().toString(CoreLabel.OutputFormat.VALUE_INDEX));
    	        	}
    	        	//System.exit(1);
    	            for(CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){  
    	                String word = token.get(CoreAnnotations.TextAnnotation.class); 
    	                if (currentWord.equals("")) {
    	                	currentWord = word;
						}
    	                else {
    	                	beforeWord = currentWord;
    	                	currentWord = word;
						}
    	                if(word.toLowerCase().equals("if") && beginIf == 0) {
    	                	beginIf = 1;
    	                }
    	                else if(beginIf == 1){
    	                	if(!word.equals(",")){
    	                		if(word.equals("."))
    	                			precondition = precondition.substring(0,precondition.length() - 1) + word + " ";
    	                		else
    	                			precondition += word + " ";
    	                	}
    	                	else {
    	                		beginIf = 0;
							}
    	                }
    	            }
    	            precondition = precondition.substring(0,precondition.length() - 1);
    	        }
    	        constraintEntity.setPrecondition(precondition);
    	        constraintEntity.setSubject(subject);
    	        constraintEntity.setPredicate("throw");
    	        constraintEntityList.add(constraintEntity);
    		}
    		//return constraintEntityList;
    	}
    	else if(type.toLowerCase().contains("return")) {
    		annotation= stanfordPipelineUtil.annotate(text);
	        System.out.println("Annotation:"+annotation.toString());
	        constraintEntityList = new ArrayList<ConstraintEntity>();
	        System.out.println("return");
			ConstraintEntity constraintEntity = new ConstraintEntity();
    		if(text.toLowerCase().contains("if") || text.toLowerCase().contains("when")) {
    	        int sentNo = 0;
    	        int beginCondition = 0;
    	        int beginReturn = 0;
    	        int beginOr = 0;
    	        String beforeWord = "";
    	        String currentWord = "";
    	        String precondition = "";
    	        String object = "";
    	        for(CoreMap sentence : stanfordPipelineUtil.extractSentenceAnnotation(annotation)){            
    	            System.out.println("Sentence #" + ++sentNo + ": " + sentence.get(CoreAnnotations.TextAnnotation.class)); 
    	            for(CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){  
    	                String word = token.get(CoreAnnotations.TextAnnotation.class); 
    	                if (currentWord.equals("")) {
    	                	currentWord = word;
						}
    	                else {
    	                	beforeWord = currentWord;
    	                	currentWord = word;
						}
    	                if((word.toLowerCase().equals("if") || word.toLowerCase().equals("when")) && beginCondition == 0) {
    	                	beginCondition = 1;
    	                	constraintEntity.setType(word);
    	                }
    	                else if(beginCondition == 1){
    	                	if(!word.equals(",")){
    	                		if(word.equals("."))
    	                			precondition = precondition.substring(0,precondition.length() - 1) + word + " ";
    	                		else
    	                			precondition += word + " ";
    	                	}
    	                	else {
    	                		beginCondition = 0;
							}
    	                }
    	                if(word.toLowerCase().contains("return") && beginReturn == 0) {
    	                	beginReturn = 1;
    	                }
    	                else if(beginReturn == 1 && beginCondition != 1) {
    	                	if (word.equals(","))
    	                		beginReturn = 0;
    	                	else
    	                		object += word + " ";
    	                }
    	                if (beginOr == 1 && beginCondition != 1) {
    	                	object += word + " ";
						}
    	                if (beforeWord.equals(",") && currentWord.equals("or")) {
    	                	object = object.substring(0,object.length() - 1);
    	                	if (!precondition.equals("")) {
    	                		precondition = precondition.substring(0,precondition.length() - 1);
    	                		constraintEntity.setPrecondition(precondition);
							}
    	                	System.out.println(constraintEntity.getPrecondition() == null);
            	            constraintEntity.setObject(object);
    	        	        constraintEntity.setSubject(subject);
    	        	        constraintEntity.setPredicate("return");
    	        	        constraintEntityList.add(constraintEntity);
    	        	        constraintEntity = new ConstraintEntity();
    	        	        beginOr = 1;
    	        	        beginCondition = 0;
    	        	        beginReturn = 0;
    	        	        precondition = "";
    	        	        object = "";
						}
    	            }
    	            object = object.substring(0,object.length() - 1);
    	            if (!precondition.equals("")) {
                		precondition = precondition.substring(0,precondition.length() - 1);
                		constraintEntity.setPrecondition(precondition);
					}
    	            constraintEntity.setObject(object);
        	        constraintEntity.setSubject(subject);
        	        constraintEntity.setPredicate("return");
        	        constraintEntityList.add(constraintEntity);
    	        }
    		}
    		System.out.println(constraintEntityList.size());
    		//return constraintEntityList;
    	}
    	else {
    		annotation= stanfordPipelineUtil.annotate(text);
	        constraintEntityList = new ArrayList<ConstraintEntity>();
			ConstraintEntity constraintEntity = new ConstraintEntity();
			constraintEntity.setSubject(subject);
    		if(text.contains("be") || text.contains("is") || text.contains("are")) {
    			int sentNo = 0;
    			String beforeWord = "";
    	        String currentWord = "";
    	        int beginOBJ = 0;
    	        String object = "";
    			for(CoreMap sentence : stanfordPipelineUtil.extractSentenceAnnotation(annotation)){            
    	            System.out.println("Sentence #" + ++sentNo + ": " + sentence.get(CoreAnnotations.TextAnnotation.class)); 
    	            for(CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){  
    	                String word = token.get(CoreAnnotations.TextAnnotation.class); 
    	                //System.out.println(token.tag());
    	                if (currentWord.equals("")) {
    	                	currentWord = word;
						}
    	                else {
    	                	beforeWord = currentWord;
    	                	currentWord = word;
						}
    	                if (beginOBJ == 1) {
    	                	if(token.tag().equals("RB") || token.tag().equals("JJ") || token.tag().contains("NN"))
    	                		object += currentWord + " ";
    	                	else if (token.tag().equals("TO")) {
    	                		object = "";
							}
    	                	else if (token.tag().equals("IN")) {
    	                		object += currentWord + " ";
    	                		object = object.substring(0,object.length() - 1);
    	                		constraintEntity.setPredicate(object);
    	                		object = "";
							}
    	                	else {
    	                		beginOBJ = 0;
    	                		if (currentWord.contains("derive") && object.contains("a class")) {
    	                			SemanticGraph dependencies = stanfordPipelineUtil.extractDependencyGraphForSentence(sentence);
    	                			for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
    	            	        		if ((edge.getRelation().toString().equals("nmod") && edge.getSource().value().contains(currentWord))) {
    	            	        			object = edge.getTarget().value() + " ";
    	        							break;
    	        						}
    	            	        	}
    	                		}
    	                	}
						}
    	                if (currentWord.equals("be") && beforeWord.equals("not")) {
    	                	object += beforeWord + " ";
						}
    	                if (beforeWord.equals("be") || beforeWord.equals("is") || beforeWord.equals("are")) {
							if (token.tag().contains("VB")) {
								constraintEntity.setPredicate(currentWord);
								SemanticGraph dependencies = stanfordPipelineUtil.extractDependencyGraphForSentence(sentence);
			    	        	for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
			    	        		if ((edge.getRelation().toString().equals("advmod") && edge.getSource().value().contains(currentWord))) {
			    	        			object += edge.getTarget().value() + " ";
									}
			    	        	}
							}
							else if(token.tag().equals("RB") || token.tag().equals("JJ") || token.tag().equals("DT")){
								constraintEntity.setPredicate(beforeWord);
								beginOBJ = 1;
								object += currentWord + " ";
							}
						}
    	            }
    			}
    			if (!object.equals(""))
    				object = object.substring(0,object.length() - 1);
	        	constraintEntity.setObject(object);
    			constraintEntityList.add(constraintEntity);
        	}
    		System.out.println(222);
		}
    	//System.exit(1);
        annotation= stanfordPipelineUtil.annotate(text);
        System.out.println("Annotation:"+annotation.toString());
        int sentNo = 0;
        for(CoreMap sentence : stanfordPipelineUtil.extractSentenceAnnotation(annotation)){
        	Tree tree = stanfordPipelineUtil.extractParseTreeForSentence(sentence);
        	System.out.println(tree.pennString());
//        	System.out.println(tree);
//        	System.out.println(tree.toString().contains("(S"));
        	System.out.println("--------SemanticGraph--------");
        	SemanticGraph dependencies = stanfordPipelineUtil.extractDependencyGraphForSentence(sentence);
        	System.out.println(dependencies.toString(SemanticGraph.OutputFormat.LIST));
        }
        return constraintEntityList;
    }
    public static void main(String[] args) {
    	ConstraintExtractor ce = new ConstraintExtractor();
    	
    	//Return TestCase
    	//List<ConstraintEntity> list = ce.extract("Return the view in this Window that currently has focus, or null if there are none.","getCurrentFocus","return");
    	//List<ConstraintEntity> list = ce.extract("Return the Transition that will be used to move Views out of the scene when starting a new Activity.","getExitTransition","return");
    	//List<ConstraintEntity> list = ce.extract("Returns true if this task was cancelled before it completed normally.","isCancelled","return");
    	
    	//Exception TestCase
    	//List<ConstraintEntity> list = ce.extract("If there was no Activity found to run the given Intent, throw ActivityNotFoundException.","startActivity","exception");
    	//List<ConstraintEntity> list = ce.extract("AsyncTask throws Exception if id is null","AsyncTask","exception");
    	//List<ConstraintEntity> list = ce.extract("AsyncTask throws Exception if a second execution is attempted.","AsyncTask","exception");
    	//List<ConstraintEntity> list = ce.extract("This method throws ActivityNotFoundException if there was no Activity found to run the given Intent.","startActivity","exception");
    	
    	//Description Null TestCase
    	//List<ConstraintEntity> list = ce.extract("AsyncTask can be executed only once (an exception will be thrown if a second execution is attempted.)","AsyncTask","description");
    	//List<ConstraintEntity> list = ce.extract("AsyncTask can be not null","AsyncTask","description");
    	//List<ConstraintEntity> list = ce.extract("AsyncTask can not be null","AsyncTask","description");
    	//List<ConstraintEntity> list = ce.extract("AsyncTask can be equal to not null","AsyncTask","description");
    	
    	//Description Type TestCase
    	//List<ConstraintEntity> list = ce.extract("AsyncTask is a instance of Task","AsyncTask","description");
    	List<ConstraintEntity> list = ce.extract("AsyncTask is a class derived from thread of java","AsyncTask","description");
    	
    	//List<ConstraintEntity> list = ce.extract("AsyncTasks should ideally be used for short operations (a few seconds at the most.)","AsyncTask","description");
    	//List<ConstraintEntity> list = ce.extract("None of parameter is null.","AsyncTask","description");
    	//List<ConstraintEntity> list = ce.extract("ZoneId is the time-zone to use to convert the instant to date-time, not null","AsyncTask","description");
    	//List<ConstraintEntity> list = ce.extract("This method may take several seconds to complete, so it should only be called from a worker thread.)","AsyncTask","description");
    	for (int i = 0; i < list.size(); i++) {
			System.out.println("list index ：   " + i);
			System.out.println("subject :  " + list.get(i).getSubject());
			System.out.println("predicate :  " + list.get(i).getPredicate());
			System.out.println("object :  " + list.get(i).getObject());
			System.out.println("precondition :  " + list.get(i).getPrecondition());
			System.out.println("type :  " + list.get(i).getType());
		}
    }
}
