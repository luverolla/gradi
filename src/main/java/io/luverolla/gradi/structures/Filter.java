package io.luverolla.gradi.structures;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface Filter<E extends BaseEntity>
{
	boolean test(E entity);
	
	default Set<E> filter(Collection<E> src)
	{
		return src.stream().filter(e -> test(e)).collect(Collectors.toSet());
	}
}
