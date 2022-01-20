package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.ResourceType;

public class ResourceTypeFilterName extends Filter<ResourceType, String>
{
    @Override
    public boolean test(ResourceType entity)
    {
        return entity.getName().trim().toLowerCase().contains(getValue().trim().toLowerCase());
    }
}
