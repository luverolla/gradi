package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;

import lombok.AllArgsConstructor;

import java.util.Comparator;

@AllArgsConstructor
public class UserComparatorPermissions implements Comparator<User>
{
    private Boolean desc = false;

    @Override
    public int compare(User o1, User o2)
    {
        return (desc ? -1 : 1) * (o1.getPermissions().size() - o2.getPermissions().size());
    }
}
