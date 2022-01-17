package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.ResourcePermission;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ResourcePermissionRepository extends PagingAndSortingRepository<ResourcePermission, Long>
{
    @Modifying
    @Transactional
    @Query("delete from ResourcePermission p where p.resource = ?1")
    void resetResource(Resource r);
}
