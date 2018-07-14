package cn.edu.fudan.se.DataExtractor.codeAnalyze;

public class CodeImport extends VariableType{
	private int importId;
	private String className;
	private String packageName;
	private int codeClassId;
	
	public CodeImport(){}
	
	public int getImportId() {
		return importId;
	}
	public void setImportId(int importId) {
		this.importId = importId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getCodeClassId() {
		return codeClassId;
	}

	public void setCodeClassId(int codeClassId) {
		this.codeClassId = codeClassId;
	}
}
