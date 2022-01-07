package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourceType;
import io.luverolla.gradi.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ResourceRepository extends JpaRepository<Resource, String>
{
    /**
     * Makes a paged query. Index are used instead of OFFSET...LIMIT
     * @param page the zero-based page offset
     * @param limit maximum number of resources to be retrieved
     * @return set of retrieved resources
     */
    @Query("select distinct r from Resource r where r.index between ?1 * ?2 and ?1 * ?2 + ?2")
    Set<Resource> findAll(int page, int limit);

    @Query("select r from Resource r where r.code = ?1")
    Resource findOne(String code);

    @Query("delete from Resource r where r.code = ?1")
    void deleteOne(String code);

    /**
     * Gets all resources of given type
     * @param t given type
     * @return set of resources
     */
    @Query("select distinct r from Resource r where r.type = ?1")
    Set<Resource> findAllByType(ResourceType t);

    /**
     * Gets all resources created by given user
     * @param u given user
     * @return set of resources
     *
     * @see io.luverolla.gradi.entities.ResourcePermission.Type
     */
    @Query("select distinct r from Resource r inner join r.permissions p where p.user = ?1 and p.type = 2")
    Set<Resource> findAllCreatedBy(User u);
}

