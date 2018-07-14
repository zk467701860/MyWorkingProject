package extraction.schema.entity;

/**
 * the entity construct with text
 */
public class APIEntity extends Entity {
    private String name;

    public APIEntity(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.addProperty("name", this.name);
    }

}
