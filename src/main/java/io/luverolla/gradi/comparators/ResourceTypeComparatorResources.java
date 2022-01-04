package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.structures.EntityComparator;

/**
 * Compares on number of resources of that type
 */
public class ResourceTypeComparatorResources extends EntityComparator<ResourceType>
{
    @Override
    public int apply(ResourceType o1, ResourceType o2)
    {
        return o1.getResources().size() - o2.getResources().size();
    }
}
