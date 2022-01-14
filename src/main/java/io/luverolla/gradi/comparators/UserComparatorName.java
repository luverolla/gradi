package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;

import java.util.Comparator;

public class UserComparatorName implements Comparator<User>
{
    @Override
    public int compare(User o1, User o2)
    {
        return o1.getName().trim().compareToIgnoreCase(o2.getName().trim());
    }
}
