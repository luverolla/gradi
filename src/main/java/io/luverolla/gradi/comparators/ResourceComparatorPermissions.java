package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.structures.EntityComparator;

public class ResourceComparatorPermissions extends EntityComparator<Resource>
{
    @Override
    public int apply(Resource r1, Resource r2)
    {
        return r1.getPermissions().size() - r2.getPermissions().size();
    }
}
