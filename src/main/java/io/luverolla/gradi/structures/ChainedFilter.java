package io.luverolla.gradi.structures;

import java.util.List;

public class ChainedFilter<E extends BaseEntity> implements Filter<E>
{
	private List<Filter<E>> filters;
	
	@SafeVarargs
	public ChainedFilter(Filter<E>... fltrs)
	{
		filters = List.of(fltrs);
	}
	
	@Override
	public boolean test(E entity)
	{
		boolean res = true;
		
		for(Filter<E> f : filters)
			res = res && f.test(entity);
		
		return res;
	}
}
