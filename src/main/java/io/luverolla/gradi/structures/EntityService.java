package io.luverolla.gradi.structures;

import java.util.*;
import java.util.stream.Collectors;

import com.sun.istack.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

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
	protected abstract Map<String, EntityComparator<E>> getComparatorMap();

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
	protected abstract Map<String, EntityFilter<E, ?>> getFilterMap();

	/**
	 * Gets JPA repository for given entity type
	 *
	 * @return JPA repository
	 */
	protected abstract JpaRepository<E, Long> repo();

	/**
	 * Gets an instance of the right comparator, from the Comparators map, according to sorting string
	 *
	 * Sorting string is in the format {$PROPERTY},{ASC|DESC}. Case doesn't matter.
	 *
	 * @param s sorting string
	 * @return instance of comparator class or <code>null</code> in case of invalid sorting string
	 */
	protected Comparator<E> getComparator(@NotNull String s)
    {
		Map<String, EntityComparator<E>> MAP = getComparatorMap();
		
        // parameter order is in form '{PROP},{ASC|DESC}'
        String prop = s.split(",")[0].toLowerCase();
        String way = s.split(",")[1].toLowerCase();

        // sanity check
        if(!MAP.containsKey(prop) || !List.of("asc", "desc").contains(way))
            return null;

		EntityComparator<E> result = MAP.get(prop);
		result.setDesc(way.equals("desc"));

        return result;
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
    protected EntityFilter<E, ?> getFilter(Map<String, ?> m)
    {
    	if(m == null)
    		return null;
    	
    	Map<String, EntityFilter<E, ?>> MAP = getFilterMap();
    	
    	// there is only one entry
    	String key = m.keySet().iterator().next();
		Object value = m.get(key);
    	
        // sanity check
        if(!MAP.containsKey(key))
            return null;

        EntityFilter<E, ?> result = MAP.get(key);
		result.setValue(value);

        return null;
    }

	/**
	 * Creates a chained comparator, according to REST request
	 * @param req REST request
	 * @return chained comparator class, or <code>null</code> in case of invalid request
	 */
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
		
		return new ChainedComparator<E>(res);
	}

	/**
	 * Creates a chained filter, according to REST request
	 * @param req REST request
	 * @return chained filter class, or <code>null</code> in case of invalid request
	 */
	protected ChainedFilter<E> chainFilters(EntitySetRequest<E> req)
	{
		List<EntityFilter<E, ?>> res = new ArrayList<>();
		
		if(req.getFilters() == null)
			return null;
		
		req.getFilters().forEach((k, v) ->
		{
			EntityFilter<E, ?> f = getFilter(Map.of(k, v));
			if(f != null)
				res.add(f);
		});
		
		return new ChainedFilter<E>(res);
	}

	/**
	 * Gets a set of entities, sorting and filtering according to REST request
	 * @param req REST request
	 * @return entity set
	 */
	public Set<E> get(EntitySetRequest<E> req)
	{
		Comparator<E> cmpt = chainComparators(req);
		EntityFilter<E, ?> fltr = chainFilters(req);

		return repo().findAll().stream()
			.filter(e -> fltr == null || fltr.test(e))
				.collect(Collectors.toCollection(() -> new TreeSet<>(cmpt)));
	}

	/**
	 * Gets a single entity by specifying its code
	 * @param code the given code
	 * @return the target entity, or <code>null</code> if not found
	 */
	public E get(String code)
	{
		return repo().findAll().stream()
			.filter(e -> e.getCode().equalsIgnoreCase(code))
			.findFirst().orElse(null);
	}

	/**
	 * Adds a set of entities
	 * @param data the given set
	 * @return set of saved entities with generated codes
	 */
	public Set<E> add(Set<E> data)
	{
		return new HashSet<>(repo().saveAll(data));
	}

	/**
	 * Update existing entity with new data
	 * @param code entity's code
	 * @param data new entity data
	 * @return updated entity, or <code>null</code> if not found
	 */
	public E update(String code, E data)
	{
		if(get(code) == null)
			return null;
		
		data.setCode(code);
		return repo().save(data);
	}

	/**
	 * Deletes the entity with given code
	 *
	 * If entity is not found, nothing happens, and no warning is sent back
	 *
	 * @param code the given code
	 */
	public void delete(String code)
	{
		E target = get(code);
		
		if(target != null)
			repo().delete(target);
	}
}
