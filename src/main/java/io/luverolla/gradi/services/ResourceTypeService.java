package io.luverolla.gradi.services;

import io.luverolla.gradi.comparators.*;
import io.luverolla.gradi.entities.ResourceProperty;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.filters.*;
import io.luverolla.gradi.repositories.ResourcePropertyRepository;
import io.luverolla.gradi.repositories.ResourceTypeRepository;
import io.luverolla.gradi.structures.EntityService;
import io.luverolla.gradi.structures.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResourceTypeService extends EntityService<ResourceType>
{
    @Autowired
    private ResourceTypeRepository typeRepo;

    @Autowired
    private ResourcePropertyRepository propRepo;

    @Override
    public PagingAndSortingRepository<ResourceType, String> repo()
    {
        return typeRepo;
    }

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

    @Override
    public ResourceType update(String code, ResourceType data)
    {
        Optional<ResourceType> tg = typeRepo.findById(code);
        if(tg.isEmpty())
            throw new NoSuchElementException();

        ResourceType found = tg.get();
        found.setName(data.getName());
        found.setDescription(data.getDescription());

        return found;
    }

    /**
     * Retrieves single resource property
     *
     * @param typeCode resource type's code
     * @param propName property's name
     *
     * @throws NoSuchElementException if type or property don't exist
     * @return {@link ResourceProperty} object, if it exists
     */
    public ResourceProperty getProperty(String typeCode, String propName)
    {
        return get(typeCode).getProperties().stream()
            .filter(p -> p.getName().equalsIgnoreCase(typeCode + "#" + propName))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }

    /**
     * Adds properties to resource type
     *
     * Properties are given a unique name with format: <code>{TYPE_CODE}#{PROPERTY_NAME}</code>
     *
     * @param typeCode given resource type's code
     * @param data collection of resource property
     *
     * @throws NoSuchElementException if resource type doesn't exist
     * @return saved objects, with unique names
     */
    public Set<ResourceProperty> addProperties(String typeCode, Collection<ResourceProperty> data)
    {
        ResourceType found = get(typeCode);

        for(ResourceProperty p : data)
        {
            p.setResourceType(found);
            p.setName(typeCode + "#" + p.getName());
        }

        return new HashSet<>((List<ResourceProperty>) propRepo.saveAll(data));
    }

    /**
     * Updates resource property with new data
     *
     * Only {@link ResourceProperty#Type} is updated
     *
     * @param typeCode resource type's code
     * @param propName property's name
     * @param data new property object
     *
     * @throws NoSuchElementException if resource type or property don't exist
     * @return updated property object, no errors occur
     */
    public ResourceProperty updateProperty(String typeCode, String propName, ResourceProperty data)
    {
        // once set, name and resource type cannot be altered
        ResourceProperty found = getProperty(typeCode, propName);
        found.setType(data.getType());

        return found;
    }

    /**
     * Deletes resource property
     *
     * @param typeCode resource type's code
     * @param propName property's name
     *
     * @throws NoSuchElementException if type or property don't exist
     */
    public void deleteProperty(String typeCode, String propName)
    {
        propRepo.delete(getProperty(typeCode, propName));
    }
}
