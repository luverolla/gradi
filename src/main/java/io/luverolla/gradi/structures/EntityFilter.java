package io.luverolla.gradi.structures;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityFilter<E extends BaseEntity>
{
    private Predicate<E> rule;

    public boolean test(E entity)
    {
        return rule.test(entity);
    }

    public Set<E> filter(Collection<E> src)
    {
        return src.stream().filter(rule).collect(Collectors.toSet());
    }
}
