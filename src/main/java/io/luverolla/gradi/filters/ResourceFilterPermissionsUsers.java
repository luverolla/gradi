package io.luverolla.gradi.filters;

import java.util.Collection;

import io.luverolla.gradi.entities.Resource;

/**
 * Filter resource by permissions users
 */
public class ResourceFilterPermissionsUsers extends Filter<Resource, Collection<String>>
{
    @Override
    public boolean test(Resource entity)
    {
        return entity.getPermissionsUsers().stream()
            .anyMatch(v -> getValue().contains(v.getCode()));
    }
}
