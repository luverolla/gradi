package io.luverolla.gradi.comparators;

import io.luverolla.gradi.entities.CodedEntity;

import java.util.Comparator;

public class EntityComparatorCode<E extends CodedEntity> implements Comparator<E>
{
    @Override
    public int compare(E o1, E o2)
    {
        return o1.getCode().trim().compareToIgnoreCase(o2.getCode().trim());
    }
}
