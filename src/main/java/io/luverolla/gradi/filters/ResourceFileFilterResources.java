package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceFile;
import io.luverolla.gradi.structures.Filter;

import java.util.Set;

public class ResourceFileFilterResources extends Filter<ResourceFile, Set<Resource>>
{
    @Override
    public boolean test(ResourceFile entity)
    {
        return getValue().contains(entity.getResource());
    }
}
