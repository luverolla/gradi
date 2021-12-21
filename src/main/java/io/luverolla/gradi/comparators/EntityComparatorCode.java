package io.luverolla.gradi.comparators;

import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.EntityComparator;

public class EntityComparatorCode<E extends CodedEntity> extends EntityComparator<E>
{
    @Override
    public int apply(E o1, E o2)
    {
        return o1.getCode().compareToIgnoreCase(o2.getCode());
    }
}
