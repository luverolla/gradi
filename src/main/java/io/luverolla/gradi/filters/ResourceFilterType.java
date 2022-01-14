package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.structures.Filter;

import java.util.Set;

public class ResourceFilterType extends Filter<Resource, Set<ResourceType>>
{
    @Override
    public boolean test(Resource r)
    {
        return getValue().contains(r.getType());
    }
}
