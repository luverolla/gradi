package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.ResourceType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, String>
{
    /**
     * Makes a paged query. Index are used instead of OFFSET...LIMIT
     * @param page the zero-based page offset
     * @param limit maximum number of resources to be retrieved
     * @return set of retrieved resources
     */
    @Query("select distinct t from ResourceType t where t.index between ?1 * ?2 and ?1 * ?2 + ?2")
    Set<ResourceType> findAll(int page, int limit);

    @Query("select t from ResourceType t where t.code = ?1")
    ResourceType findOne(String code);

    @Query("delete from ResourceType t where t.code = ?1")
    void deleteOne(String code);
}