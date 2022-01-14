package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.Resource;

import java.util.Comparator;

/**
 * Sorts resources by number of permissions
 */
public class ResourceComparatorPermissionsSize implements Comparator<Resource>
{
    @Override
    public int compare(Resource r1, Resource r2)
    {
        return r1.getPermissions().size() - r2.getPermissions().size();
    }
}
