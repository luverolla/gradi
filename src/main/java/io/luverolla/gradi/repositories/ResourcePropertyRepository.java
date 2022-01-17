package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.ResourceProperty;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ResourcePropertyRepository extends PagingAndSortingRepository<ResourceProperty, String>
{
    @Query("select p from ResourceProperty p where p.name = ?1")
    Optional<ResourceProperty> findByName(String name);
}
