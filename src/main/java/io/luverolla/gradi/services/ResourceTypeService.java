package io.luverolla.gradi.services;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.repositories.ResourcePropertyRepository;
import io.luverolla.gradi.repositories.ResourceTypeRepository;
import io.luverolla.gradi.rest.EntitySetRequest;
import io.luverolla.gradi.structures.CodedEntity;
import io.luverolla.gradi.structures.EntityComparator;
import io.luverolla.gradi.structures.EntityFilter;
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

    @Autowired
    private ResourcePropertyRepository propRepo;

    @Override
    protected Map<String, EntityComparator<ResourceType>> getComparatorMap()
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
    protected Map<String, EntityFilter<ResourceType, ?>> getFilterMap()
    {
        return Map.ofEntries(
            Map.entry("code", new EntityFilterCode<>()),
            Map.entry("createdAt", new EntityFilterCreatedAt<>()),
            Map.entry("updatedAt", new EntityFilterUpdatedAt<>()),
            Map.entry("name", new ResourceTypeFilterName()),
            Map.entry("resources", new ResourceTypeFilterResources())
        );
    }

    public SortedSet<ResourceType> getTypes(EntitySetRequest<ResourceType> req)
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

    public Set<ResourceType> addTypes(Set<ResourceType> data)
    {
        for(ResourceType t : data)
        {
            // 36^4 -> 10000_base36, this will avoid zero-starting codes, which are reserved
            int num = typeRepo.findAll().size() + (int)Math.pow(36, 4);
            t.setCode(CodedEntity.toBase36(5, num));
        }

        return new HashSet<>(typeRepo.saveAll(data));
    }

    public ResourceType updateType(String code, ResourceType data)
    {
        ResourceType tg = typeRepo.findOne(code);

        if(tg != null)
        {
            tg.setName(data.getName());
            tg.setDescription(data.getDescription());
        }

        return tg;
    }

    public void delete(String code)
    {
        typeRepo.deleteOne(code);
    }
}
