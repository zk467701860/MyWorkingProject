package cn.edu.fudan.se.DataExtractor.codeAnalyze;

public class CodeField extends VariableType{
	private int codeFieldId;
	private int classId;
    private String fieldName;
    private String fieldType;

    public int getCodeFieldId() {
		return codeFieldId;
	}

	public void setCodeFieldId(int codeFieldId) {
		this.codeFieldId = codeFieldId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	

}
