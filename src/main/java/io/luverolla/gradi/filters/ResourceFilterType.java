package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;

import java.util.Collection;

/**
 * Gets all resources whose type's codes are included in a given collection
 */
public class ResourceFilterType extends Filter<Resource, Collection<String>>
{
    @Override
    public boolean test(Resource r)
    {
        return getValue().contains(r.getType().getCode());
    }
}
