package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.DatedEntity;

import java.util.Comparator;

public class EntityComparatorCreatedAt<E extends DatedEntity> implements Comparator<E>
{
    @Override
    public int compare(E o1, E o2)
    {
        return o1.getCreatedAt().compareTo(o2.getCreatedAt());
    }
}
