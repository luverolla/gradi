package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;

import lombok.AllArgsConstructor;

import java.util.Comparator;

@AllArgsConstructor
public class UserComparatorRole implements Comparator<User>
{
    private Boolean desc = false;

    @Override
    public int compare(User o1, User o2)
    {
        return (desc ? -1 : 1) * o1.getRole().compareTo(o2.getRole());
    }
}
