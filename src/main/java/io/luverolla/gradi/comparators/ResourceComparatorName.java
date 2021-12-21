package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.structures.EntityComparator;

public class ResourceComparatorName extends EntityComparator<Resource>
{
    @Override
    public int apply(Resource r1, Resource r2)
    {
        return r1.getName().compareToIgnoreCase(r2.getName());
    }
}
