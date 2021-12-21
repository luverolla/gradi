package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.structures.EntityComparator;

public class UserComparatorSurname extends EntityComparator<User>
{
    @Override
    public int apply(User o1, User o2)
    {
        return o1.getSurname().compareToIgnoreCase(o2.getSurname());
    }
}
