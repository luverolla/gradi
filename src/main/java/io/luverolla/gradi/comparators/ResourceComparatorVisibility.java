package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.structures.EntityComparator;

public class ResourceComparatorVisibility extends EntityComparator<Resource>
{
    @Override
    public int apply(Resource r1, Resource r2)
    {
        return r1.getVisibility().compareTo(r2.getVisibility());
    }
}
