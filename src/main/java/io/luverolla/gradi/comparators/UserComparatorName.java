package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor
@AllArgsConstructor
public class UserComparatorName implements Comparator<User>
{
    private boolean desc = false;

    @Override
    public int compare(User o1, User o2)
    {
        return (desc ? -1 : 1) * o1.getName().compareToIgnoreCase(o2.getName());
    }
}
