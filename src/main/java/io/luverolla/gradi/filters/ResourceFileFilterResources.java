package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.ResourceFile;

import java.util.Collection;

public class ResourceFileFilterResources extends Filter<ResourceFile, Collection<String>>
{
    @Override
    public boolean test(ResourceFile entity)
    {
        return getValue().contains(entity.getResource().getCode());
    }
}
