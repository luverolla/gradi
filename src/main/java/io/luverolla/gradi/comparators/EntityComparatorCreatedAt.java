package io.luverolla.gradi.comparators;

import io.luverolla.gradi.structures.DatedEntity;
import io.luverolla.gradi.structures.EntityComparator;

public class EntityComparatorCreatedAt<E extends DatedEntity> extends EntityComparator<E>
{
    @Override
    public int apply(E o1, E o2)
    {
        return o1.getCreatedAt().compareTo(o2.getCreatedAt());
    }
}
