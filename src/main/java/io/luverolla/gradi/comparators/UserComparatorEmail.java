package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;

import java.util.Comparator;

public class UserComparatorEmail implements Comparator<User>
{
    @Override
    public int compare(User o1, User o2)
    {
        return o1.getEmail().trim().compareToIgnoreCase(o2.getEmail().trim());
    }
}
