package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.ResourceFile;

public class ResourceFileFilterName extends Filter<ResourceFile, String>
{
    @Override
    public boolean test(ResourceFile entity)
    {
        return entity.getName().trim().toLowerCase().contains(getValue().trim().toLowerCase());
    }
}
