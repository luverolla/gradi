package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.EntityComparator;

public class UserComparatorEmail extends EntityComparator<User>
{
    @Override
    public int apply(User o1, User o2)
    {
        return o1.getEmail().compareToIgnoreCase(o2.getEmail());
    }
}
