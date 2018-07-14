package cn.edu.fudan.se.DataExtractor.codeAnalyze;

import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedList;

import cn.edu.fudan.se.DataExtractor.mybatisDao.CodeAnalyzeDao;
import cn.edu.fudan.se.DataExtractor.mybatisDao.DeleteDao;

public class InsertTest {
	
	@Test
	public void testField(){
		CodeField field = new CodeField();
		field.setFieldName("test");
		field.setClassId(2);
		field.setType(TYPE_VALUE.TYPE_CLASS);
		field.setInnerClassId(1);
		
		CodeAnalyzeDao.getInstance().insertField(field);
	}
	
	@Test
	public void testListField(){
		CodeField field = new CodeField();
		field.setFieldName("test");
		field.setClassId(2);
		field.setType(TYPE_VALUE.TYPE_CLASS);
		field.setInnerClassId(1);
		CodeField field1 = new CodeField();
		field1.setFieldName("test");
		field1.setClassId(2);
		field1.setType(TYPE_VALUE.TYPE_CLASS);
		field1.setInnerClassId(1);
		CodeField field2 = new CodeField();
		field2.setFieldName("test");
		field2.setClassId(2);
		field2.setType(TYPE_VALUE.TYPE_CLASS);
		field2.setInnerClassId(1);
		
		LinkedList<CodeField> fieldList = new LinkedList<CodeField>();
		fieldList.add(field);
		fieldList.add(field1);
		fieldList.add(field2);
		CodeAnalyzeDao.getInstance().insertField(fieldList);
	}
	
	@Ignore
	public void testParameter(){
		CodeParameter parameter = new CodeParameter();
		parameter.setCodeMethodId(1);
		parameter.setParameterType("sdads");
		CodeAnalyzeDao.getInstance().insertParameter(parameter);
	}
	
	@Test
	public void testParameterList(){
		CodeParameter parameter = new CodeParameter();
		parameter.setCodeMethodId(1);
		parameter.setParameterType("sdads");
		CodeParameter parameter1 = new CodeParameter();
		parameter.setCodeMethodId(1);
		parameter.setParameterType("sdads");
		CodeParameter parameter2 = new CodeParameter();
		parameter.setCodeMethodId(1);
		parameter.setParameterType("sdads");
		CodeParameter parameter3 = new CodeParameter();
		parameter.setCodeMethodId(1);
		parameter.setParameterType("sdads");
		
		LinkedList<CodeParameter> parameters = new LinkedList<CodeParameter>();
		parameters.add(parameter);
		parameters.add(parameter1);
		parameters.add(parameter2);
		parameters.add(parameter3);
		CodeAnalyzeDao.getInstance().insertParameters(parameters);
	}
	
	@Test
	public void testListParameter(){
		CodeParameter parameter = new CodeParameter();
		parameter.setCodeMethodId(1);
		parameter.setParameterType("sdads");
		CodeParameter parameter1 = new CodeParameter();
		parameter1.setCodeMethodId(2);
		parameter1.setParameterType("sdads");
		
		LinkedList<CodeParameter> parameterList = new LinkedList<CodeParameter>();
		parameterList.add(parameter);
		parameterList.add(parameter1);
		CodeAnalyzeDao.getInstance().insertParameters(parameterList);
		
	}
	
	@Test
	public void testCodeMethod(){
		CodeMethod method = new CodeMethod();
		method.setAccess("public");
		method.setCodeClassId(1);
		method.setMethodName("name");
		method.setMethodSignature("signature");
		CodeAnalyzeDao.getInstance().insertNewMethod(method);
	}
	
	@Test
	public void testImportNode(){
		CodeImport importNode = new CodeImport();
		importNode.setClassName("className");
		importNode.setCodeClassId(1);
		importNode.setPackageName("packageName");
		CodeAnalyzeDao.getInstance().insertImportNode(importNode);
	}
	
	@Test
	public void testImportNodeList(){
		CodeImport importNode = new CodeImport();
		importNode.setClassName("className");
		importNode.setCodeClassId(1);
		importNode.setPackageName("packageName");
		CodeImport importNode1 = new CodeImport();
		importNode.setClassName("className");
		importNode.setCodeClassId(1);
		importNode.setPackageName("packageName");
		CodeImport importNode2 = new CodeImport();
		importNode.setClassName("className");
		importNode.setCodeClassId(1);
		importNode.setPackageName("packageName");
		
		LinkedList<CodeImport> importList = new LinkedList<CodeImport>();
		importList.add(importNode);
		importList.add(importNode1);
		importList.add(importNode2);
		
		CodeAnalyzeDao.getInstance().insertImportNodeList(importList);
	}
	
	@Test
	public void testCodeClass(){
		CodeClass c = new CodeClass();
		c.setClassName("haha");
		c.setPackageName("package");
		c.setPath("path");
		c.setReleaseId(1);
		
		CodeAnalyzeDao.getInstance().insertCodeClass(c);
	}
	
	
	public static void main(String[] args){
		InsertTest test =new InsertTest();
		test.testCodeMethod();
	}
}
