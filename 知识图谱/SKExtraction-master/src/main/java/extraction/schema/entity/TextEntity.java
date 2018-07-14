package extraction.schema.entity;

/**
 * the entity only contain text
 * it is the basic entity type.
 * used for those entity you can't classify
 */
public class TextEntity extends Entity {
    private String name;

    public TextEntity(String name) {
        this.setName(name);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.addProperty("name",this.name);
    }

}
