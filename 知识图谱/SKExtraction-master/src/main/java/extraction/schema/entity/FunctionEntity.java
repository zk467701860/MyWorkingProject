package extraction.schema.entity;

/**
 * the
 */
public class FunctionEntity extends Entity {
    private String description;

    public FunctionEntity(String description) {
        this.setDescription(description);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.addProperty("description",this.description);
    }

}
