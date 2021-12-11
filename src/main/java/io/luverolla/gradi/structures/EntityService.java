package io.luverolla.gradi.structures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.data.jpa.repository.JpaRepository;

import io.luverolla.gradi.rest.EntitySetRequest;

public abstract class EntityService<E extends BaseEntity>
{
	private JpaRepository<E, Long> repo;
	
	protected abstract Comparator<E> getComparator(String s);
	protected abstract Filter<E> getFilter(Map<String, Object> m);
	
	protected ChainedComparator<E> chainComparators(EntitySetRequest<E> req)
	{
		List<Comparator<E>> res = new ArrayList<>();
		
		if(req.getOrders().isEmpty())
			return null;
		
		req.getOrders().get().forEach(s ->
		{
			Comparator<E> cmp = getComparator(s);
			if(cmp != null)
				res.add(cmp);
		});
		
		return new ChainedComparator<>((Comparator<E>[]) res.toArray());
	}
	
	protected ChainedFilter<E> chainFilters(EntitySetRequest<E> req)
	{
		List<Filter<E>> res = new ArrayList<>();
		
		if(req.getFilters().isEmpty())
			return null;
		
		req.getFilters().get().forEach((k, v) ->
		{
			Filter<E> f = getFilter(Map.of(k, v));
			if(f != null)
				res.add(f);
		});
		
		return new ChainedFilter<>((Filter<E>[]) res.toArray());
	}
	
	public Set<E> get(EntitySetRequest<E> req)
	{
		ChainedComparator<E> cmpt = chainComparators(req);
		ChainedFilter<E> fltr = chainFilters(req);
		
		SortedSet<E> res = new TreeSet<>(cmpt);
		res.addAll(fltr.filter(repo.findAll()));
		
		return res;
	}
}
