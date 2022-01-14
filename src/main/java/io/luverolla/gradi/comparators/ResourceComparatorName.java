package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;

import java.util.Comparator;

public class ResourceComparatorName implements Comparator<Resource>
{
    @Override
    public int compare(Resource r1, Resource r2)
    {
        return r1.getName().trim().compareToIgnoreCase(r2.getName().trim());
    }
}
