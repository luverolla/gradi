package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.ResourceFile;

import java.util.Comparator;

public class ResourceFileComparatorName implements Comparator<ResourceFile>
{
    @Override
    public int compare(ResourceFile o1, ResourceFile o2)
    {
        return o1.getName().trim().compareToIgnoreCase(o2.getName().trim());
    }
}
