package cn.edu.fudan.se.DataExtractor.codeAnalyze;

import java.util.List;

public class CodeMethod {
	private int codeMethodId;
	private int codeClassId;
	private String methodName;
	private String methodContent;
	private String access;
	private String methodSignature;
	private String modifier;
	private CodeParameter returnValue;
	private List<CodeParameter> parameterList;
	
	public CodeMethod(){}
	
	public int getCodeClassId() {
		return codeClassId;
	}

	public void setCodeClassId(int codeClassId) {
		this.codeClassId = codeClassId;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}
	public int getCodeMethodId() {
		return codeMethodId;
	}

	public void setCodeMethodId(int codeMethodId) {
		this.codeMethodId = codeMethodId;
	}

	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodContent() {
		return methodContent;
	}
	public void setMethodContent(String methodContent) {
		this.methodContent = methodContent;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String acess) {
		this.access = acess;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public CodeParameter getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(CodeParameter returnValue) {
		this.returnValue = returnValue;
	}

	public List<CodeParameter> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<CodeParameter> parameterList) {
		this.parameterList = parameterList;
	}
}
