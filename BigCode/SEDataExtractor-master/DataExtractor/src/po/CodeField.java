package po;

public class CodeField {
    private Integer codeFieldId;

    private String fieldName;

    private String fieldType;

    private String typeValue;

    private Integer innerClass;

    private Integer api;

    public Integer getCodeFieldId() {
        return codeFieldId;
    }

    public void setCodeFieldId(Integer codeFieldId) {
        this.codeFieldId = codeFieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType == null ? null : fieldType.trim();
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue == null ? null : typeValue.trim();
    }

    public Integer getInnerClass() {
        return innerClass;
    }

    public void setInnerClass(Integer innerClass) {
        this.innerClass = innerClass;
    }

    public Integer getApi() {
        return api;
    }

    public void setApi(Integer api) {
        this.api = api;
    }
}