package io.luverolla.gradi.structures;

import java.util.*;

import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.rest.EntitySetRequest;

/**
 * Generic service class with essential CRUD and utility methods
 *
 * @param <E> entity type, derived from {@link CodedEntity}
 */
public abstract class EntityService<E extends CodedEntity>
{
	/**
	 * Get comparators map for entity type.
	 *
	 * Comparators map is a <code>(key, value)</code> map, where:
	 * <ul>
	 *     <li><code>key</code> is the name of a property belonging to the given entity type</li>
	 *     <li><code>value</code> is a comparator class, under the <code>comparators</code> subpackage</li>, that compares according to that property
	 * </ul>
	 *
	 * @return comparators map
	 */
	protected abstract Map<String, Comparator<E>> getComparatorMap();

	/**
	 * Get filters map for entity type.
	 *
	 * Filters map is a <code>(key, value)</code> map, where:
	 * <ul>
	 *     <li><code>key</code> is the name of a property belonging to the given entity type</li>
	 *     <li><code>value</code> is a filter class, under the <code>filters</code> subpackage</li>, that filters according to that property
	 * </ul>
	 *
	 * @return filters map
	 */
	protected abstract Map<String, Filter<E, ?>> getFilterMap();

	/**
	 * Gets an instance of the right comparator, from the Comparators map, according to sorting map
	 *
	 * Map is in <code>(key, value)</code> format, where:
	 * <ul>
	 *     <li><code>key</code> is the property's name we want to sort by</li>
	 *     <li><code>value</code> is the sorting way, and can be <code>asc</code> or <code>desc</code></li>
	 * </ul>
	 *
	 * @param m sorting map
	 * @return instance of comparator class or <code>null</code> in case of invalid sorting string
	 */
	protected Comparator<E> getComparator(Map.Entry<String, String> m)
    {
		if(m == null)
			return null;

		Map<String, Comparator<E>> MAP = getComparatorMap();

        // check if property name is valid
        if(!MAP.containsKey(m.getKey()))
			throw new InvalidPropertyException();

		Comparator<E> result = MAP.get(m.getKey());
		return m.getValue().equals("desc") ? result.reversed() : result;
    }

	/**
	 * Gets an instance of the right filter, from the Filters map, according to filter rule
	 *
	 * Filter rule is a <code>(key, value)</code> map, where:
	 * <ul>
	 *     <li><code>key</code> is the property we want to filter by</li>
	 *     <li><code>value</code> is the value, or the values, the filter takes as arguments</li>
	 * </ul>
	 *
	 * @param m filter rule
	 * @return instance of filter class or <code>null</code> in case of invalid filter rule
	 */
    protected Filter<E, ?> getFilter(Map.Entry<String, ?> m)
    {
    	if(m == null)
    		return null;
    	
    	Map<String, Filter<E, ?>> MAP = getFilterMap();
    	
        // sanity check
        if(!MAP.containsKey(m.getKey()))
            throw new InvalidPropertyException();

        Filter<E, ?> result = MAP.get(m.getKey());
		result.setValue(m.getValue());

        return result;
    }

	/**
	 * Creates a chained comparator, according to REST request
	 * @param req REST request
	 * @return chained comparator class, or <code>null</code> in case of invalid request
	 */
	protected ChainedComparator<E> chainComparators(EntitySetRequest req)
	{
		List<Comparator<E>> res = new ArrayList<>();
		
		if(req.getOrders() == null)
			return null;
		
		req.getOrders().forEach((k, v) ->
		{
			Comparator<E> cmp = getComparator(Map.entry(k, v));
			if(cmp != null)
				res.add(cmp);
		});
		
		return new ChainedComparator<E>(res);
	}

	/**
	 * Creates a chained filter, according to REST request
	 * @param req REST request
	 * @return chained filter class, or <code>null</code> in case of invalid request
	 */
	protected ChainedFilter<E> chainFilters(EntitySetRequest req)
	{
		List<Filter<E, ?>> res = new ArrayList<>();
		
		if(req.getFilters() == null)
			return null;
		
		req.getFilters().forEach((k, v) ->
		{
			Filter<E, ?> f = getFilter(Map.entry(k, v));
			if(f != null)
				res.add(f);
		});
		
		return new ChainedFilter<E>(res);
	}
}
