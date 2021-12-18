package io.luverolla.gradi.structures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;

import io.luverolla.gradi.rest.EntitySetRequest;

public abstract class EntityService<E extends CodedEntity>
{	
	protected abstract Map<String, Class<? extends Comparator<E>>> getComparatorMap();
	protected abstract Map<String, Class<? extends Filter<E>>> getFilterMap();
	protected abstract JpaRepository<E, Long> repo();
	
	protected Comparator<E> getComparator(String s)
    {
		Map<String, Class<? extends Comparator<E>>> MAP = getComparatorMap();
		
        // parameter order is in form '{PROP},{ASC|DESC}'
        String prop = s.split(",")[0].toLowerCase();
        String way = s.split(",")[1].toLowerCase();

        // sanity check
        if(!MAP.containsKey(prop) || !List.of("asc", "desc").contains(way))
            return null;

        try
        {
            // our comparator's constructors take a 'desc' boolean varible as argument, if order is descendent
            return (Comparator<E>) MAP.get(prop).getDeclaredConstructor(Boolean.class).newInstance(way.equals("desc"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    protected Filter<E> getFilter(Map<String, Object> m)
    {
    	if(m == null)
    		return null;
    	
    	Map<String, Class<? extends Filter<E>>> MAP = getFilterMap();
    	
    	// there is only one entry
    	String key = m.keySet().iterator().next();
    	Object value = m.get(key);
    	
        // sanity check
        if(!MAP.containsKey(key))
            return null;

        try
        {
            // our filter's constructors take a 'value' object
            return (Filter<E>) MAP.get(key).getDeclaredConstructor(Object.class).newInstance(value);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
	
	protected ChainedComparator<E> chainComparators(EntitySetRequest<E> req)
	{
		List<Comparator<E>> res = new ArrayList<>();
		
		if(req.getOrders() == null)
			return null;
		
		req.getOrders().forEach(s ->
		{
			Comparator<E> cmp = getComparator(s);
			if(cmp != null)
				res.add(cmp);
		});
		
		return new ChainedComparator<>(res);
	}
	
	protected ChainedFilter<E> chainFilters(EntitySetRequest<E> req)
	{
		List<Filter<E>> res = new ArrayList<>();
		
		if(req.getFilters() == null)
			return null;
		
		req.getFilters().forEach((k, v) ->
		{
			Filter<E> f = getFilter(Map.of(k, v));
			if(f != null)
				res.add(f);
		});
		
		return new ChainedFilter<>(res);
	}
	
	public Set<E> get(EntitySetRequest<E> req)
	{
		Comparator<E> cmpt = chainComparators(req);
		Filter<E> fltr = chainFilters(req);
		
		SortedSet<E> res = new TreeSet<>(cmpt);
		List<E> tmp = repo().findAll();
		
		if(fltr != null)
			tmp.stream().filter(e -> fltr.test(e));
		
		tmp.forEach(e -> res.add(e));
		
		return res;
	}
	
	public E get(String code)
	{
		return repo().findAll().stream()
			.filter(e -> e.getCode().equalsIgnoreCase(code))
			.findFirst().orElse(null);
	}
	
	public Set<E> add(Set<E> data)
	{
		return repo().saveAll(data).stream()
			.collect(Collectors.toSet());
	}
	
	public E update(String code, E data)
	{
		if(get(code) == null)
			return null;
		
		data.setCode(code);
		return repo().save(data);
	}
	
	public void delete(String code)
	{
		E target = get(code);
		
		if(target != null)
			repo().delete(target);
	}
	
	public Set<E> getAll()
    {
    	return repo().findAll().stream().collect(Collectors.toSet());
    }
}
