package extraction.schema.graph;

import com.sun.istack.internal.NotNull;
import extraction.schema.entity.Entity;
import extraction.schema.relation.Relation;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Relation> relationList;
    private List<Entity> entities;

    public Graph() {
        relationList = new ArrayList<>();
        entities = new ArrayList<>();
    }

    public void addRelation(@NotNull Relation relation) {
        this.relationList.add(relation);
    }

    public void addEntities(@NotNull Entity entity) {
        this.entities.add(entity);
    }
}
