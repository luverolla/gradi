package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.ResourceType;

import java.util.Comparator;

public class ResourceTypeComparatorName implements Comparator<ResourceType>
{
    @Override
    public int compare(ResourceType r1, ResourceType r2)
    {
        return r1.getName().trim().compareToIgnoreCase(r2.getName().trim());
    }
}
