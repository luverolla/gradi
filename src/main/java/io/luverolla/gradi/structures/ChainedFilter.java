package io.luverolla.gradi.structures;

import java.util.Collection;

/**
 * Filter created by combination of other filters for same entity type
 * @param <E> entity type, derived from {@link BaseEntity}
 */
public class ChainedFilter<E extends BaseEntity> extends EntityFilter<E, Object>
{
	private final Collection<EntityFilter<E, ?>> filters;
	
	public ChainedFilter(Collection<EntityFilter<E, ?>> fltrs)
	{
		filters = fltrs;
	}
	
	@Override
	public boolean test(E entity)
	{
		boolean res = true;
		
		for(EntityFilter<E, ?> f : filters)
			res = res && f.test(entity);
		
		return res;
	}
}
