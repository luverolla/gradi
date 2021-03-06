package io.luverolla.gradi.filters;

import io.luverolla.gradi.structures.DatedEntity;
import io.luverolla.gradi.structures.Filter;

import java.time.OffsetDateTime;
import java.util.List;

public class EntityFilterUpdatedAt<E extends DatedEntity> extends Filter<E, List<String>>
{
    @Override
    public boolean test(E e)
    {
        OffsetDateTime min = getValue().get(0) == null ? null : OffsetDateTime.parse(getValue().get(0));
        OffsetDateTime max = getValue().get(1) == null ? null : OffsetDateTime.parse(getValue().get(1));

        boolean res = true;

        if(min != null)
            res = res && e.getUpdatedAt().compareTo(min) >= 0;

        if(max != null)
            res = res && e.getUpdatedAt().compareTo(max) <= 0;

        return res;
    }
}
