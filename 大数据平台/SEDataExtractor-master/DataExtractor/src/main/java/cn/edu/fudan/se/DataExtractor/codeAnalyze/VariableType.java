package cn.edu.fudan.se.DataExtractor.codeAnalyze;

import cn.edu.fudan.se.DataExtractor.api.APIClass;

public class VariableType {
    private String type;
    private int innerClassId=0;
    private CodeClass innerClass;
    private int apiId=0;
    private APIClass apiClass;
    
	public int getInnerClassId() {
		return innerClassId;
	}
	public void setInnerClassId(int innerClassId) {
		this.innerClassId = innerClassId;
	}
	public CodeClass getInnerClass() {
		return innerClass;
	}
	public void setInnerClass(CodeClass innerClass) {
		if(innerClass != null){
			innerClassId = innerClass.getCodeClassId();
		}
		this.innerClass = innerClass;
	}
	public int getApiId() {
		return apiId;
	}
	public void setApiId(int apiId) {
		this.apiId = apiId;
	}
	public APIClass getApiClass() {
		return apiClass;
	}
	public void setApiClass(APIClass apiClass) {
		if(apiClass != null){
			this.apiId = apiClass.getId();
		}
		this.apiClass = apiClass;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
