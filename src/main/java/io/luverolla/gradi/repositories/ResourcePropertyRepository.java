package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.ResourceProperty;

import io.luverolla.gradi.entities.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResourcePropertyRepository extends JpaRepository<ResourceProperty, String>
{
    @Query("select p from ResourceProperty p where p.name = ?1")
    ResourceProperty findOneByName(String name);
}
