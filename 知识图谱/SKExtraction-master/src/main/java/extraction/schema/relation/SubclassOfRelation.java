package extraction.schema.relation;

import extraction.schema.entity.Entity;

import java.util.Map;

public class SubclassOfRelation extends Relation {

    public static final String SUBCLASS_OF = "subclass of";

    public SubclassOfRelation(Entity subject, Entity object, Map<String, String> propertyMap) {
        super(subject, SUBCLASS_OF, object, propertyMap);
    }

    public SubclassOfRelation(Entity subject, Entity object) {
        super(subject, SUBCLASS_OF, object);
    }

}
