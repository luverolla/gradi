package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourcePermission;
import io.luverolla.gradi.structures.EntityFilter;

import java.util.Set;

public class ResourceFilterPermissions extends EntityFilter<Resource, Set<ResourcePermission>>
{
    @Override
    public boolean test(Resource r)
    {
        return r.getPermissions().stream().anyMatch(getValue()::contains);
    }
}
