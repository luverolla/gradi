package io.luverolla.gradi.services;

import io.luverolla.gradi.comparators.EntityComparatorCode;
import io.luverolla.gradi.comparators.EntityComparatorCreatedAt;
import io.luverolla.gradi.comparators.EntityComparatorUpdatedAt;
import io.luverolla.gradi.entities.ResourceProperty;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.filters.EntityFilterCode;
import io.luverolla.gradi.filters.EntityFilterCreatedAt;
import io.luverolla.gradi.filters.EntityFilterUpdatedAt;
import io.luverolla.gradi.repositories.ResourcePropertyRepository;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.Filter;
import io.luverolla.gradi.structures.EntityService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class ResourcePropertyService extends EntityService<ResourceProperty>
{
    @Autowired
    private ResourcePropertyRepository repo;

    @Override
    protected Map<String, Comparator<ResourceProperty>> getComparatorMap()
    {
        return Map.ofEntries(
            Map.entry("code", new EntityComparatorCode<>()),
            Map.entry("createdAt", new EntityComparatorCreatedAt<>()),
            Map.entry("updatedAt", new EntityComparatorUpdatedAt<>())
        );
    }

    @Override
    protected Map<String, Filter<ResourceProperty, ?>> getFilterMap()
    {
        return Map.ofEntries(
            Map.entry("code", new EntityFilterCode<>()),
            Map.entry("createdAt", new EntityFilterCreatedAt<>()),
            Map.entry("updatedAt", new EntityFilterUpdatedAt<>())
        );
    }

    public Set<ResourceProperty> getProperties(EntitySetRequest req)
    {
        Integer page = req.getPage(), lim = req.getLimit();
        boolean paging = page != null && lim != null;

        Collection<ResourceProperty> data = paging ? repo.findAll(page, lim) : repo.findAll();

        return data.stream().filter(r -> chainFilters(req).test(r))
            .collect(Collectors.toCollection(() -> new TreeSet<>(chainComparators(req))));
    }

    public ResourceProperty getOneProperty(String code)
    {
        Optional<ResourceProperty> prop = repo.findById(code);
        return prop.orElseThrow(NoSuchElementException::new);
    }

    public Set<ResourceProperty> getProperties(ResourceType type)
    {
        return repo.findAllByType(type);
    }

    public Set<ResourceProperty> addProperties(Set<ResourceProperty> data)
    {
        for(ResourceProperty p : data)
            p.setCode(CodedEntity.nextCode());

        return data;
    }

    public ResourceProperty updateOneProperty(String code, ResourceProperty data)
    {
        Optional<ResourceProperty> tg = repo.findById(code);
        if(tg.isEmpty())
            throw new NoSuchElementException();

        ResourceProperty found = tg.get();
        found.setName(data.getName());
        found.setType(data.getType());
        found.setResourceType(data.getResourceType());

        return repo.save(found);
    }

    public void deleteOneProperty(String code)
    {
        repo.delete(getOneProperty(code));
    }
}
