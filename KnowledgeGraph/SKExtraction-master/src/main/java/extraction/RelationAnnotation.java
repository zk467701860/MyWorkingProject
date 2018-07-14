package extraction;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.util.ErasureUtils;
import extraction.schema.relation.Relation;

import java.util.List;

/**
 * annotation for relations extract from sentence
 */
public class RelationAnnotation implements CoreAnnotation<List<Relation>> {
    @Override
    public Class<List<Relation>> getType() {
        return ErasureUtils.uncheckedCast(List.class);
    }
}
