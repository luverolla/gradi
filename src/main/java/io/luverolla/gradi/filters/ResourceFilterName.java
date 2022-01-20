package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;

public class ResourceFilterName extends Filter<Resource, String>
{
    @Override
    public boolean test(Resource r)
    {
        return r.getName().trim().toLowerCase().contains(getValue().trim().toLowerCase());
    }
}
