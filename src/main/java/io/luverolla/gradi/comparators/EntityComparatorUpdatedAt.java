package io.luverolla.gradi.comparators;

import io.luverolla.gradi.structures.DatedEntity;
import io.luverolla.gradi.structures.EntityComparator;

public class EntityComparatorUpdatedAt<E extends DatedEntity> extends EntityComparator<E>
{
    @Override
    public int apply(E o1, E o2)
    {
        return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
    }
}
