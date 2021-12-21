package io.luverolla.gradi.filters;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.structures.EntityFilter;

public class ResourceFilterName extends EntityFilter<Resource, String>
{
    @Override
    public boolean test(Resource r)
    {
        return r.getName().toLowerCase().contains(getValue().toLowerCase());
    }
}
