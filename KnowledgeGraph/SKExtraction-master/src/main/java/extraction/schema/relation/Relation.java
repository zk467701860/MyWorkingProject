package extraction.schema.relation;


import extraction.schema.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * a class that store relation Triple.
 * the edu.stanford.nlp.ie.util.RelationTriple for more complex design example
 */
public class Relation {
    private Entity subject;
    private String relationType;
    private Entity object;
    private Map<String, String> propertyMap;

    /**
     * An optional score (confidence) for this triple
     */
    public final double confidence;

    public Relation(Entity subject,
                    String relationType,
                    Entity object,
                    Map<String, String> propertyMap) {
        this(subject, relationType, object, propertyMap, 1.0d);
    }

    public Relation(Entity subject,
                    String relationType,
                    Entity object,
                    Map<String, String> propertyMap,
                    double confidence) {
        this.subject = subject;
        this.object = object;
        this.relationType = relationType;
        this.propertyMap = propertyMap;
        this.confidence = confidence;
    }

    public Relation(Entity subject,
                    String relationType,
                    Entity object
    ) {
        this(subject, relationType, object, new HashMap<>());
    }

    public void addProperty(String propertyName, String propertyValue) {
        this.propertyMap.put(propertyName, propertyValue);
    }

    public Entity getSubject() {
        return subject;
    }

    public void setSubject(Entity subject) {
        this.subject = subject;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Entity getObject() {
        return object;
    }

    public void setObject(Entity object) {
        this.object = object;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

    @Override
    public String toString() {
        return subject.toString() + " " + relationType + " " + object.toString();
    }
}
