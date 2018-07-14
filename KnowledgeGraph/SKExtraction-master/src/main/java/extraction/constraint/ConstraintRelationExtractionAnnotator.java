package extraction.constraint;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import extraction.RelationExtractionAnnotator;
import extraction.schema.entity.APIEntity;
import extraction.schema.entity.TextEntity;
import extraction.schema.entity.ConstraintEntity;
import extraction.schema.relation.Relation;

import java.util.ArrayList;
import java.util.List;

import processor.StanfordPipelineFactory;
import processor.StanfordPipelineUtil;

/**
 * the relation extraction for some constraint,
 * mainly focus on the directive-type sentences.
 *
 */
public class ConstraintRelationExtractionAnnotator extends RelationExtractionAnnotator {
	
	private StanfordPipelineUtil stanfordPipelineUtil;
	private String subject = "AsyncTask";
	private String type = "description";

    public ConstraintRelationExtractionAnnotator() {
        stanfordPipelineUtil = StanfordPipelineFactory.getDefaultNLPPipeline();
    }
    @Override
    public void annotate(Annotation annotation) {
        //todo complete complete the relation extract method
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        int sentNo = 0;
        for(CoreMap coreMap:sentences){
        	System.out.println("Sentence #" + ++sentNo + ": " + coreMap.get(CoreAnnotations.TextAnnotation.class)); 
            coreMap.set(ConstraintRelationAnnotation.class,extract(coreMap,subject,type));
        }
    }
    public List<Relation> extract(CoreMap coreMap,String subject,String type) {
    	//针对异常的描述
    	List<Relation> relationList=new ArrayList<Relation>();
    	Annotation annotation = null;
    	String text = coreMap.get(CoreAnnotations.TextAnnotation.class);
    	if (text.contains("(") && text.contains(")")) {
    		String sentence1 = text.substring(0,text.indexOf('(')) + text.substring(text.indexOf(')') + 1);
    		String sentence2 = text.substring(text.indexOf('(') + 1, text.indexOf(')'));
			//System.out.println("split sentence1:  " + sentence1);
			//System.out.println("split sentence2:  " + sentence2);
			annotation= stanfordPipelineUtil.annotate(sentence2);
			Tree tree = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(TreeCoreAnnotations.TreeAnnotation.class);
			//判断括号中的句子是不是独立的句子
			if (tree.toString().contains("(S") && sentence2.toLowerCase().contains("exception") && sentence2.toLowerCase().contains("throw")) {
				relationList.addAll(extract(annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0),subject,"exception"));
				relationList.addAll(extract(stanfordPipelineUtil.annotate(sentence1).get(CoreAnnotations.SentencesAnnotation.class).get(0),subject,type));
				return relationList;
			}
		}
    	if(type.toLowerCase().contains("exception")) {
    		APIEntity subjectEntity;
    		APIEntity objectEntity = null;
    		ConstraintEntity constraintEntity = new ConstraintEntity();
    		if(text.toLowerCase().contains("if")) {
    			constraintEntity.setConditionType("if");
    			String precondition = "";
    	        int beginIf = 0;
    	        String beforeWord = "";
    	        String currentWord = "";
	            System.out.println("  Parse exception sentence :  " + text); 
	            SemanticGraph dependencies = coreMap.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
	        	//System.out.println(dependencies.toString(SemanticGraph.OutputFormat.LIST));
	            //找到throw后面的宾语
	        	for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
	        		if ((edge.getRelation().toString().equals("dobj") && edge.getSource().value().contains("throw")) || edge.getRelation().toString().equals("nsubjpass") && edge.getSource().value().contains("throw")) {
						System.out.println(edge.getTarget().value());
						objectEntity = new APIEntity(edge.getTarget().value());
						break;
					}
	        		//System.out.println(edge.getRelation().toString());
	        		//System.out.println(edge.getSource().toString(CoreLabel.OutputFormat.VALUE_INDEX));
	        		//System.out.println(edge.getTarget().toString(CoreLabel.OutputFormat.VALUE_INDEX));
	        	}
	        	//System.exit(1);
	            for(CoreLabel token : coreMap.get(CoreAnnotations.TokensAnnotation.class)){  
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
	        
    	        constraintEntity.setPrecondition(precondition);
    	        subjectEntity = new APIEntity(subject);
    	        constraintEntity.setType("exception");;
    	        relationList.add(new Relation(subjectEntity,"has constraint",constraintEntity));
    	        relationList.add(new Relation(constraintEntity,"throw exception",objectEntity));
    		}
    		//return constraintEntityList;
    	}
    	else if(type.toLowerCase().contains("return")) {
    		System.out.println("  Parse return sentence :  " + text); 
			ConstraintEntity constraintEntity = new ConstraintEntity();
			APIEntity subjectEntity;
			//存在条件句
    		if(text.toLowerCase().contains("if") || text.toLowerCase().contains("when")) {
    	        int sentNo = 0;
    	        int beginCondition = 0;
    	        int beginReturn = 0;
    	        int beginOr = 0;
    	        String beforeWord = "";
    	        String currentWord = "";
    	        String precondition = "";
    	        String object = ""; 
	            for(CoreLabel token : coreMap.get(CoreAnnotations.TokensAnnotation.class)){  
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
	                	constraintEntity.setConditionType(word);
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
	                //存在两种情况返回值，如"Return the view in this Window that currently has focus, or null if there are none."
	                if (beforeWord.equals(",") && currentWord.equals("or")) {
	                	object = object.substring(0,object.length() - 1);
	                	if (!precondition.equals("")) {
	                		precondition = precondition.substring(0,precondition.length() - 1);
	                		constraintEntity.setPrecondition(precondition);
						}
	                	subjectEntity = new APIEntity(subject);
        	            constraintEntity.setConstraintDescription(object);
	        	        constraintEntity.setType("return");
	        	        relationList.add(new Relation(subjectEntity,"has constraint",constraintEntity));
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
	            subjectEntity = new APIEntity(subject);
	            constraintEntity.setConstraintDescription(object);
    	        constraintEntity.setType("return");
    	        relationList.add(new Relation(subjectEntity,"has constraint",constraintEntity));
    		}
    		//return constraintEntityList;
    	}
    	else {
    		System.out.println("  Parse description sentence :  " + text); 
    		annotation= stanfordPipelineUtil.annotate(text);
			ConstraintEntity constraintEntity = new ConstraintEntity();
			APIEntity subjectEntity = new APIEntity(subject);
			//处理类似should be, must be, can be或者be动词其他形式的陈述句
    		if(text.contains("be") || text.contains("is") || text.contains("are") || text.contains("was")) {
    			int sentNo = 0;
    			String beforeWord = "";
    	        String currentWord = "";
    	        int beginOBJ = 0;
    	        String object = "";
    	        String predicate = "";
    	        int beginParentheses = 0;
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
    	                //判断动词后的各类宾语形式
    	                if (beginOBJ == 1) {
    	                	if(token.tag().equals("RB") || token.tag().equals("JJ") || token.tag().contains("NN"))
    	                		object += currentWord + " ";
    	                	//处理equal to这一类宾语
    	                	else if (token.tag().equals("TO")) {
    	                		object = "";
							}
    	                	//PROGRESS_START#:was deprecated in API level 24.谓语从in这里断开
    	                	else if (token.tag().equals("IN")) {
    	                		object += currentWord + " ";
    	                		object = object.substring(0,object.length() - 1);
    	                		predicate = object;
    	                		object = "";
							}
    	                	else {
    	                		beginOBJ = 0;
    	                		//处理"AsyncTask is a class derived from thread of java"
    	                		if (currentWord.contains("derive") && object.contains("a class")) {
    	                			SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
    	                			for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
    	                				//nmod(derived-5, thread-7)
    	            	        		if ((edge.getRelation().toString().equals("nmod") && edge.getSource().value().contains(currentWord))) {
    	            	        			object = edge.getTarget().value() + " ";
    	        							break;
    	        						}
    	            	        	}
    	                		}
    	                	}
						}
    	                //System.out.println("CW:  " + currentWord + "  token:  "  + token.tag() + "   Obj:  "  + object);
    	                //约束的宾语后如果直接跟括号分句，则由于已经判断了不是独立句子，所以直接跟在宾语后可以提供更多信息
    	                if (currentWord.equals("-LRB-") && object.contains(beforeWord) && beginParentheses == 0) {
    	                	object += "(";
    	                	beginParentheses = 1;
						}
    	                else if (!currentWord.equals("-RRB-") && beginParentheses == 1) {
    	                	object += currentWord + " ";
						}
    	                else if (currentWord.equals("-RRB-") && beginParentheses == 1) {
    	                	if (!object.equals(""))
    	        				object = object.substring(0,object.length() - 1);
    	                	object += ")" + " ";
    	                	beginParentheses = 0;
						}
    	                
    	                if (currentWord.equals("be") && beforeWord.equals("not")) {
    	                	object += beforeWord + " ";
						}
    	                if (beforeWord.equals("be") || beforeWord.equals("is") || beforeWord.equals("are") || beforeWord.equals("was")) {
    	                	//处理be动词后跟动词被动语态，句型为"AsyncTask can be executed only once"或"PROGRESS_START was deprecated in API level 24."或AsyncTasks#:should ideally be used for short operations (a few seconds at the most.)
							if (token.tag().contains("VB")) {
								predicate = currentWord;
								SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
			    	        	for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
			    	        		//advmod(executed-4, only-5) advmod(executed-4, once-6)
			    	        		if (edge.getRelation().toString().equals("advmod") && edge.getSource().value().contains(currentWord)) {
			    	        			object += edge.getTarget().value() + " ";
									}
			    	        		//nmod(deprecated-3, level-6)
			    	        		else if (edge.getRelation().toString().equals("nmod") && edge.getSource().value().contains(currentWord)) {
			    	        			int index = edge.getTarget().index();
			    	        			boolean isAddObject = false;//判断整个宾语词组中是否加入了核心宾语
			    	        			for (SemanticGraphEdge depedge : dependencies.edgeListSorted()) {
			    	        				//case(level-6, in-4)compound(level-6, API-5)nummod(level-6, 24-7) 或 amod(operations-8, short-7)
					    	        		if (depedge.getSource().value().contains(edge.getTarget().value()) && (depedge.getRelation().toString().equals("case") || depedge.getRelation().toString().equals("amod") || depedge.getRelation().toString().equals("compound") || depedge.getRelation().toString().equals("nummod") || depedge.getRelation().toString().equals("det"))) {
					    	        			if (depedge.getTarget().index() < index) {
					    	        				object += depedge.getTarget().value() + " ";
												}
					    	        			else {
													if (!isAddObject) {
														object += edge.getTarget().value() + " ";
														isAddObject = true;
													}
													object += depedge.getTarget().value() + " ";
												}
											}
			    	        			}
			    	        			//假如最后还没加入核心宾语，则加入
			    	        			if (!isAddObject) {
											object += edge.getTarget().value() + " ";
											isAddObject = true;
										}
									}
			    	        	}
							}
							//处理be动词后跟形容词或名词词组等
							else if(token.tag().equals("RB") || token.tag().equals("JJ") || token.tag().equals("DT")){
								predicate = beforeWord;
								beginOBJ = 1;
								object += currentWord + " ";
							}
						}
    	            }
    			}
    			if (!object.equals(""))
    				object = object.substring(0,object.length() - 1);
	        	constraintEntity.setConstraintDescription(predicate + " " + object);
	        	constraintEntity.setType("description");
    	        relationList.add(new Relation(subjectEntity,"has constraint",constraintEntity));
        	}
		}
        return relationList;
    }
}
