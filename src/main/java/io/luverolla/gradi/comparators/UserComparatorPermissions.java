package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;

import java.util.Comparator;

public class UserComparatorPermissions implements Comparator<User>
{
    @Override
    public int compare(User o1, User o2)
    {
        return o1.getPermissions().size() - o2.getPermissions().size();
    }
}
