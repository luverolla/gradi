package io.luverolla.gradi.services;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.repositories.ResourceTypeRepository;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.Filter;
import io.luverolla.gradi.structures.EntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResourceTypeService extends EntityService<ResourceType>
{
    @Autowired
    private ResourceTypeRepository typeRepo;

    @Override
    protected Map<String, Comparator<ResourceType>> getComparatorMap()
    {
        return Map.ofEntries(
            Map.entry("code", new EntityComparatorCode<>()),
            Map.entry("createdAt", new EntityComparatorCreatedAt<>()),
            Map.entry("updatedAt", new EntityComparatorUpdatedAt<>()),
            Map.entry("name", new ResourceTypeComparatorName()),
            Map.entry("resources", new ResourceTypeComparatorResources())
        );
    }

    @Override
    protected Map<String, Filter<ResourceType, ?>> getFilterMap()
    {
        return Map.ofEntries(
            Map.entry("code", new EntityFilterCode<>()),
            Map.entry("createdAt", new EntityFilterCreatedAt<>()),
            Map.entry("updatedAt", new EntityFilterUpdatedAt<>()),
            Map.entry("name", new ResourceTypeFilterName())
        );
    }

    public SortedSet<ResourceType> getTypes(EntitySetRequest req)
    {
        Stream<ResourceType> data;
        if(req.getPage() != null && req.getLimit() != null)
            data = typeRepo.findAll(req.getPage(), req.getLimit()).stream();
        else
            data = typeRepo.findAll().stream();

        return data.filter(e -> chainFilters(req).test(e))
            .collect(Collectors.toCollection(() -> new TreeSet<>(chainComparators(req))));
    }

    public ResourceType getOneType(String code)
    {
        return typeRepo.findOne(code);
    }

    public Set<ResourceType> addTypes(Collection<ResourceType> data)
    {
        for(ResourceType t : data)
            t.setCode(CodedEntity.nextCode());

        return new HashSet<>(typeRepo.saveAll(data));
    }

    public ResourceType updateType(String code, ResourceType data)
    {
        Optional<ResourceType> tg = typeRepo.findById(code);
        if(tg.isEmpty())
            throw new NoSuchElementException();

        ResourceType found = tg.get();
        found.setName(data.getName());
        found.setDescription(data.getDescription());

        return found;
    }

    public void delete(String code)
    {
        typeRepo.deleteOne(code);
    }
}
