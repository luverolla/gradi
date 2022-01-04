package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.structures.EntityFilter;

public class ResourceTypeFilterName extends EntityFilter<ResourceType, String>
{
    @Override
    public boolean test(ResourceType entity)
    {
        return entity.getName().toLowerCase().contains(getValue().toLowerCase());
    }
}
