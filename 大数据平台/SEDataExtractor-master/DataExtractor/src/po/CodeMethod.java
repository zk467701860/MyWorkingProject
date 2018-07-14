package po;

public class CodeMethod {
    private Integer codeMethodId;

    private Integer codeClassId;

    private String methodSignuature;

    private String methodName;

    private String access;

    private Integer isStatic;

    private String returnType;

    private String typeValue;

    private Integer innerClass;

    private Integer api;

    private String methodContent;

    public Integer getCodeMethodId() {
        return codeMethodId;
    }

    public void setCodeMethodId(Integer codeMethodId) {
        this.codeMethodId = codeMethodId;
    }

    public Integer getCodeClassId() {
        return codeClassId;
    }

    public void setCodeClassId(Integer codeClassId) {
        this.codeClassId = codeClassId;
    }

    public String getMethodSignuature() {
        return methodSignuature;
    }

    public void setMethodSignuature(String methodSignuature) {
        this.methodSignuature = methodSignuature == null ? null : methodSignuature.trim();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access == null ? null : access.trim();
    }

    public Integer getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(Integer isStatic) {
        this.isStatic = isStatic;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType == null ? null : returnType.trim();
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

    public String getMethodContent() {
        return methodContent;
    }

    public void setMethodContent(String methodContent) {
        this.methodContent = methodContent == null ? null : methodContent.trim();
    }
}