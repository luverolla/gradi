package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.structures.EntityComparator;

public class ResourceTypeComparatorName extends EntityComparator<ResourceType>
{
    @Override
    public int apply(ResourceType r1, ResourceType r2)
    {
        return r1.getName().compareToIgnoreCase(r2.getName());
    }
}
