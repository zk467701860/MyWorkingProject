package cn.edu.fudan.se.DataExtractor.codeAnalyze;

public class CodeParameter extends VariableType{
	public static String RETURN_SCOPE ="RETURN";
	public static String PARAMETER_SCOPE = "PARAMETER";
	
	private int codeParameterId;
	private int codeMethodId;
	private String parameterName;
	private String parameterScope;
	private String parameterType;
	
	public CodeParameter(){}
	public int getCodeParameterId() {
		return codeParameterId;
	}
	public void setCodeParameterId(int codeParameterId) {
		this.codeParameterId = codeParameterId;
	}
	public int getCodeMethodId() {
		return codeMethodId;
	}
	public void setCodeMethodId(int codeMethodId) {
		this.codeMethodId = codeMethodId;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterScope() {
		return parameterScope;
	}
	public void setParameterScope(String parameterScope) {
		this.parameterScope = parameterScope;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
}
