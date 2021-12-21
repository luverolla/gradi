package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.structures.EntityFilter;

import java.util.Set;

public class ResourceFilterType extends EntityFilter<Resource, Set<ResourceType>>
{
    @Override
    public boolean test(Resource r)
    {
        return getValue().contains(r.getType());
    }
}
