package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.structures.EntityFilter;

import java.util.List;

/**
 * Filter resource type by number of related resources
 */
public class ResourceTypeFilterResources extends EntityFilter<ResourceType, List<Integer>>
{
    @Override
    public boolean test(ResourceType entity)
    {
        int size = entity.getResources().size(),
            low = getValue().get(0),
            high = getValue().get(1);

        return size >= low && size <= high;
    }
}
