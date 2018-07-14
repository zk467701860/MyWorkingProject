package extraction.schema.entity;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * a basic class for all entity in the knowledge graph,
 * has a propertyMap inside.
 */
public class Entity {
    private Map<String, String> propertyMap;

    public Entity() {
        this(new HashMap<>());
    }

    public Entity(@NotNull Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }
    public void addProperty(String propertyName, String propertyValue) {
        this.propertyMap.put(propertyName, propertyValue);
    }
    public String toString(){
        return "Entity{"+this.propertyMap.toString()+"}";
    }
}
