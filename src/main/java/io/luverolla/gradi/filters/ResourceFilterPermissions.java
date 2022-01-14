package io.luverolla.gradi.filters;

import java.util.Set;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourcePermission;
import io.luverolla.gradi.structures.Filter;

/**
 * Filter resource by permissions
 * 
 * Permission set's element are put in disjuntion
 */
public class ResourceFilterPermissions extends Filter<Resource, Set<ResourcePermission>>
{
    @Override
    public boolean test(Resource entity)
    {
        return getValue().stream().anyMatch(v -> entity.getPermissions().contains(v));
    }
}
