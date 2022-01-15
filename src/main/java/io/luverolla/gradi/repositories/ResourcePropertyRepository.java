package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.ResourceProperty;

import io.luverolla.gradi.entities.ResourceType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;

public interface ResourcePropertyRepository extends PagingAndSortingRepository<ResourceProperty, String>
{
    @Query("select p from ResourceProperty p where p.name = ?1")
    public Optional<ResourceProperty> findByName(String name);

    @Query("select p from ResourceProperty p where p.type = ?1")
    public Set<ResourceProperty> findByType(ResourceType type);
}
