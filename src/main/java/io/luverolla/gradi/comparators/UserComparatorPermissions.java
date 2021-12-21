package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.EntityComparator;

public class UserComparatorPermissions extends EntityComparator<User>
{
    @Override
    public int apply(User o1, User o2)
    {
        return o1.getPermissions().size() - o2.getPermissions().size();
    }
}
