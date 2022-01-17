package io.luverolla.gradi.filters;

import java.util.Collection;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourcePermission;
import io.luverolla.gradi.structures.Filter;

/**
 * Filter resource by permissions types
 */
public class ResourceFilterPermissionsTypes extends Filter<Resource, Collection<ResourcePermission.Type>>
{
    @Override
    public boolean test(Resource entity)
    {
        return entity.getPermissionsTypes().stream()
                .anyMatch(v -> getValue().contains(v));
    }
}
