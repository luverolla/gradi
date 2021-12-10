package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor
@AllArgsConstructor
public class UserComparatorEmail implements Comparator<User>
{
    private boolean desc = false;

    @Override
    public int compare(User o1, User o2)
    {
        return (desc ? -1 : 1) * o1.getEmail().compareToIgnoreCase(o2.getEmail());
    }
}
