package io.luverolla.gradi.services;

import java.util.*;
import java.util.stream.Collectors;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.entities.ResourceProperty;
import io.luverolla.gradi.exceptions.InvalidPropertyException;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.repositories.ResourcePropertyRepository;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.repositories.ResourceRepository;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends EntityService<Resource>
{
	@Autowired
	private ResourceRepository repo;

	@Autowired
	private ResourcePropertyRepository propRepo;

	@Override
	protected Map<String, Comparator<Resource>> getComparatorMap()
	{
		return Map.ofEntries(
			Map.entry("code", new EntityComparatorCode<>()),
			Map.entry("name", new ResourceComparatorName()),
			Map.entry("createdAt", new EntityComparatorCreatedAt<>()),
			Map.entry("updatedAt", new EntityComparatorUpdatedAt<>()),
			Map.entry("type", new ResourceComparatorType()),
			Map.entry("permissions", new ResourceComparatorPermissionsSize()),
			Map.entry("visibility", new ResourceComparatorVisibility())
		);
	}

	@Override
	protected Map<String, Filter<Resource, ?>> getFilterMap()
	{
		return Map.ofEntries(
			Map.entry("code", new EntityFilterCode<>()),
			Map.entry("name", new ResourceFilterName()),
			Map.entry("createdAt", new EntityFilterCreatedAt<>()),
			Map.entry("updatedAt", new EntityFilterUpdatedAt<>()),
			Map.entry("type", new ResourceFilterType()),
			Map.entry("permissions", new ResourceFilterPermissions())
		);
	}

	/**
	 * Get right filter, taking in account custom properties' names
	 * @param m filter rule
	 * @return right Resource filter
	 */
	@Override
	protected Filter<Resource, ?> getFilter(Map.Entry<String, ?> m)
	{
		// check if map has any filter for property
		if(getFilterMap().containsKey(m.getKey()))
			return super.getFilter(m);

		// check if is custom property
		Optional<ResourceProperty> prop = propRepo.findOneByName(m.getKey());
		return prop.isEmpty() ? null : new ResourceFilterCustom(prop.get(), m.getValue());
	}

	/**
	 * Get right filter, taking in account custom properties' names
	 * @param m filter rule
	 * @return right Resource filter
	 */
	@Override
	protected Comparator<Resource> getComparator(Map.Entry<String, String> m)
	{
		// check if map has any filter for property
		if(getComparatorMap().containsKey(m.getKey()))
			return super.getComparator(m);

		// check if is custom property
		Optional<ResourceProperty> prop = propRepo.findOneByName(m.getKey());
		if(prop.isEmpty())
			throw new InvalidPropertyException();

		Comparator<Resource> comp = new ResourceComparatorCustom<>(prop.get());
		return m.getValue().equals("desc") ? comp.reversed() : comp;
	}

	public SortedSet<Resource> getResources(EntitySetRequest req)
	{
		Integer page = req.getPage(), lim = req.getLimit();
		boolean paging = page != null && lim != null;

		Collection<Resource> data = paging ? repo.findAll(page, lim) : repo.findAll();

		return data.stream().filter(r -> chainFilters(req).test(r))
			.collect(Collectors.toCollection(() -> new TreeSet<>(chainComparators(req))));
	}

    public Resource getOneResource(String code)
    {
        Optional<Resource> test = repo.findById(code);
        return test.orElseThrow(NoSuchElementException::new);
    }

	public Set<Resource> addResources(Collection<Resource> src)
	{
		for(Resource r : src)
			r.setCode(CodedEntity.nextCode());

		return new HashSet<>(repo.saveAll(src));
	}

	public Resource updateOneResource(String code, Resource data)
	{
		Resource tg = repo.findOne(code);

		if(tg != null)
		{
			tg.setName(data.getName());
			tg.setVisibility(data.getVisibility());
			tg.setDescription(data.getDescription());
			tg.setParent(data.getParent());
			tg.setType(data.getType());
		}

		return tg;
	}

	public void deleteOneResource(String code)
	{
		repo.deleteOne(code);
	}
}
