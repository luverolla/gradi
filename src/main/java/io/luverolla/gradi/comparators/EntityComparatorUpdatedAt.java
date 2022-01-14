package io.luverolla.gradi.comparators;

import io.luverolla.gradi.structures.DatedEntity;

import java.util.Comparator;

public class EntityComparatorUpdatedAt<E extends DatedEntity> implements Comparator<E>
{
    @Override
    public int compare(E o1, E o2)
    {
        return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
    }
}
