package io.luverolla.gradi.structures;

import java.util.*;
import java.util.stream.Collectors;

import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.exceptions.NoAvailableCodeException;
import io.luverolla.gradi.rest.EntitySetRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Generic service class with essential CRUD and utility methods
 *
 * @param <E> entity type, derived from {@link CodedEntity}
 */
public abstract class EntityService<E extends CodedEntity>
{
	/**
	 * UNIX timestamp (in milliseconds) of 2000-01-01T00:00:00+00:00
	 */
	public static final long YEAR2000 = 946684800000L;

	/**
	 * Converts number to base36 with up to <code>chars</code> digits
	 *
	 * Base36 extends Base16 adding the other 20 latin letters
	 *
	 * @param chars maximum number of digits
	 * @param num number to convert
	 *
	 * @return converted string
	 * @throws NoAvailableCodeException if converted number doesn't fit in given number of digits
	 */
	private static String toBase36(int chars, long num)
	{
		if(num >= Math.pow(36, chars))
			throw new NoAvailableCodeException();

		String base36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder bld = new StringBuilder(new String(new char[chars]).replace('\0', '0'));

		for(int i = chars - 1; i >= 0; i--)
		{
			if(num == 0) break;
			bld.setCharAt(i, base36.charAt( (int) (num % 36)) );
			num /= 36;
		}

		return bld.toString();
	}

	/**
	 * Gets next unique code for entity
	 *
	 * @return unique code
	 */
	public static String nextCode()
	{
		return toBase36(10, System.currentTimeMillis() - YEAR2000);
	}

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

	protected abstract PagingAndSortingRepository<E, String> repo();

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

	/**
	 * Get collection of data according to request
	 *
	 * Data is sorted, filtered and paged according to given {@link EntitySetRequest} object
	 *
	 * @param req given request object
	 *
	 * @return set of data
	 */
	public SortedSet<E> get(EntitySetRequest req)
	{
		return repo().findAll(PageRequest.of(req.getPage(), req.getLimit()))
			.filter(r -> chainFilters(req).test(r))
				.stream().collect(Collectors.toCollection(() -> new TreeSet<>(chainComparators(req))));
	}

	/**
	 * Retrieves single entity by its code
	 *
	 * @param code given entity's code
	 *
	 * @throws NoSuchElementException if entity doesn't exist
	 * @return entity object, if it exists
	 */
	public E get(String code)
	{
		return repo().findById(code).orElseThrow(NoSuchElementException::new);
	}

	/**
	 * Adds collection of entities
	 *
	 * Each entity gets an unique code assigned
	 *
	 * @param data data to add
	 *
	 * @return saved objects, with unique codes
	 *
	 * @see EntityService#nextCode()
	 */
	public Set<E> add(Collection<E> data)
	{
		for(E e : data) e.setCode(nextCode());
		return (Set<E>) repo().saveAll(data);
	}

	/**
	 * Updates given entity with new data
	 *
	 * Data to update depends on entity type, so this needs to be implemented
	 *
	 * @param code given entity's code
	 * @param data new entity data
	 *
	 * @throws NoSuchElementException if entity doesn't exist
	 * @return updated entity, if it exists
	 */
	public abstract E update(String code, E data) throws NoSuchElementException;

	/**
	 * Deletes a given entity
	 *
	 * @param code given entity's code
	 *
	 * @throws NoSuchElementException if entity doesn't exist
	 */
	public void delete(String code)
	{
		repo().delete(get(code));
	}
}