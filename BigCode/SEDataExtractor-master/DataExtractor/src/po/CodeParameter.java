package po;

public class CodeParameter {
    private Integer codeParameterId;

    private Integer codeMethodId;

    private String parameterType;

    private String typeValue;

    private Integer innerClass;

    private Integer api;

    public Integer getCodeParameterId() {
        return codeParameterId;
    }

    public void setCodeParameterId(Integer codeParameterId) {
        this.codeParameterId = codeParameterId;
    }

    public Integer getCodeMethodId() {
        return codeMethodId;
    }

    public void setCodeMethodId(Integer codeMethodId) {
        this.codeMethodId = codeMethodId;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType == null ? null : parameterType.trim();
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