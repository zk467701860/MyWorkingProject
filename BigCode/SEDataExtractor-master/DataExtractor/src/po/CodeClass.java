package po;

public class CodeClass {
    private Integer codeClassId;

    private Integer codeFileId;

    private String codePackage;

    private String className;

    private String content;

    public Integer getCodeClassId() {
        return codeClassId;
    }

    public void setCodeClassId(Integer codeClassId) {
        this.codeClassId = codeClassId;
    }

    public Integer getCodeFileId() {
        return codeFileId;
    }

    public void setCodeFileId(Integer codeFileId) {
        this.codeFileId = codeFileId;
    }

    public String getCodePackage() {
        return codePackage;
    }

    public void setCodePackage(String codePackage) {
        this.codePackage = codePackage == null ? null : codePackage.trim();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}