package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;

import java.util.Comparator;

public class ResourceComparatorType implements Comparator<Resource>
{
    @Override
    public int compare(Resource r1, Resource r2)
    {
        return r1.getType().getName().compareToIgnoreCase(r2.getType().getName());
    }
}
