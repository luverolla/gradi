package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;

import java.util.Comparator;

public class ResourceComparatorVisibility implements Comparator<Resource>
{
    @Override
    public int compare(Resource r1, Resource r2)
    {
        return r1.getVisibility().compareTo(r2.getVisibility());
    }
}
