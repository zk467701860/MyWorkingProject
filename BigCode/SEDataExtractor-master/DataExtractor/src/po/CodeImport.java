package po;

public class CodeImport {
    private Integer codeImportId;

    private String importPackage;

    private String importClass;

    private String importType;

    private Integer innerClass;

    private Integer api;

    public Integer getCodeImportId() {
        return codeImportId;
    }

    public void setCodeImportId(Integer codeImportId) {
        this.codeImportId = codeImportId;
    }

    public String getImportPackage() {
        return importPackage;
    }

    public void setImportPackage(String importPackage) {
        this.importPackage = importPackage == null ? null : importPackage.trim();
    }

    public String getImportClass() {
        return importClass;
    }

    public void setImportClass(String importClass) {
        this.importClass = importClass == null ? null : importClass.trim();
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType == null ? null : importType.trim();
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