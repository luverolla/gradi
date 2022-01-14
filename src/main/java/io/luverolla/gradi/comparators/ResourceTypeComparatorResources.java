package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.ResourceType;

import java.util.Comparator;

/**
 * Compares on number of resources of that type
 */
public class ResourceTypeComparatorResources implements Comparator<ResourceType>
{
    @Override
    public int compare(ResourceType o1, ResourceType o2)
    {
        return o1.getResources().size() - o2.getResources().size();
    }
}
