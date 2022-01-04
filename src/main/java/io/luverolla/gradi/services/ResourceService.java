package io.luverolla.gradi.services;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.entities.ResourceProperty;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.repositories.ResourcePropertyRepository;
import io.luverolla.gradi.structures.EntityComparator;
import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.repositories.ResourceRepository;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.EntityFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends EntityService<Resource>
{
	private static final Map<String, EntityComparator<Resource>> CMPTS = Map.ofEntries(
		Map.entry("code", new EntityComparatorCode<>()),
		Map.entry("name", new ResourceComparatorName()),
		Map.entry("createdAt", new EntityComparatorCreatedAt<>()),
		Map.entry("updatedAt", new EntityComparatorUpdatedAt<>()),
		Map.entry("type", new ResourceComparatorType()),
		Map.entry("permissions", new ResourceComparatorPermissions()),
		Map.entry("visibility", new ResourceComparatorVisibility())
	);

	private static final Map<String, EntityFilter<Resource, ?>> FLTRS = Map.ofEntries(
		Map.entry("code", new EntityFilterCode<>()),
		Map.entry("name", new ResourceFilterName()),
		Map.entry("createdAt", new EntityFilterCreatedAt<>()),
		Map.entry("updatedAt", new EntityFilterUpdatedAt<>()),
		Map.entry("type", new ResourceFilterType()),
		Map.entry("permissions", new ResourceFilterPermissions())
	);

	@Autowired
	private ResourceRepository repo;

	@Autowired
	private ResourcePropertyRepository propRepo;

	@Override
	protected Map<String, EntityComparator<Resource>> getComparatorMap()
	{
		return CMPTS;
	}

	@Override
	protected Map<String, EntityFilter<Resource, ?>> getFilterMap()
	{
		return FLTRS;
	}

	@Override
	protected JpaRepository<Resource, Long> repo()
	{
		return repo;
	}

	/**
	 * Get right filter, taking in account custom properties' names
	 * @param m filter rule
	 * @return right Resource filter
	 */
	@Override
	protected EntityFilter<Resource, ?> getFilter(Map.Entry<String, ?> m)
	{
		// check if map has any filter for property
		if(FLTRS.containsKey(m.getKey()))
			return super.getFilter(m);

		// check if is custom property
		ResourceProperty prop = getPropertyByName(m.getKey());
		return new ResourceFilterCustom(prop, m.getValue());
	}

	/**
	 * Get right filter, taking in account custom properties' names
	 * @param m filter rule
	 * @return right Resource filter
	 */
	@Override
	protected EntityComparator<Resource> getComparator(Map.Entry<String, String> m)
	{
		// check if map has any filter for property
		if(CMPTS.containsKey(m.getKey()))
			super.getComparator(m);

		// check if is custom property
		ResourceProperty prop = getPropertyByName(m.getKey());
		return new ResourceComparatorCustom<>(prop, m.getValue().equals("desc"));
	}

	/**
	 * Gets all resource properties of given resource type
	 * @param type resource type
	 * @return resource property set
	 */
	private Set<ResourceProperty> getResourceProperties(ResourceType type)
	{
		return propRepo.findAll().stream()
			.filter(p -> p.getResourceType().equals(type))
				.collect(Collectors.toSet());
	}

	/**
	 * Gets resource property by its name
	 *
	 * <p>Matching is performed as case-insensitive</p>
	 *
	 * @param name property's name
	 *
	 * @throws InvalidPropertyException if no property of given name exists
	 * @return {@link ResourceProperty} object, if it exists
	 */
	private ResourceProperty getPropertyByName(String name)
	{
		return propRepo.findAll().stream()
			.filter(p -> p.getName().equalsIgnoreCase(name))
				.findFirst().orElseThrow(InvalidPropertyException::new);
	}
}
