package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.ResourceProperty;
import io.luverolla.gradi.entities.ResourceType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ResourcePropertyRepository extends JpaRepository<ResourceProperty, String>
{
    @Query("select distinct p from ResourceProperty p where p.index between ?1 * ?2 and ?1 * ?2 + ?2")
    Set<ResourceProperty> findAll(int page, int limit);

    @Query("select p from ResourceProperty p where p.name = ?1")
    Optional<ResourceProperty> findOneByName(String name);

    @Query("select p from ResourceProperty p where p.type = ?1")
    Set<ResourceProperty> findAllByType(ResourceType type);
}
