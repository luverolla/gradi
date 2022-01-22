package io.luverolla.gradi.structures;

import java.util.Collection;

/**
 * Filter created by combination of other filters for same entity type
 *
 * @param <E> entity type
 */
public class ChainedFilter<E> extends Filter<E, Object>
{
	private final Collection<Filter<E, ?>> filters;
	
	public ChainedFilter(Collection<Filter<E, ?>> fltrs)
	{
		filters = fltrs;
	}
	
	@Override
	public boolean test(E entity)
	{
		boolean res = true;
		
		for(Filter<E, ?> f : filters)
			res = res && f.test(entity);
		
		return res;
	}
}
