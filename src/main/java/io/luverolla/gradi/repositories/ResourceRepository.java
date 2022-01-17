package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.Resource;
import io.luverolla.gradi.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ResourceRepository extends PagingAndSortingRepository<Resource, String>
{
    @Query("select distinct r from Resource r join r.permissions p where r.visibility <> 2 or (p.user = ?1 and p.type >= 0)")
    Page<Resource> findAllUserRead(User u, Pageable pg);

    @Query("select distinct r from Resource r join r.permissions p where r.visibility <> 2 or (p.user = ?1 and p.type > 0)")
    Page<Resource> findAllUserWrite(User u, Pageable pg);

    @Query("select r from Resource r join r.permissions p where (r.visibility <> 2 or (p.user = ?1 and p.type >= 0)) and r.code = ?2")
    Optional<Resource> findOneUserRead(User u, String code);

    @Query("select r from Resource r join r.permissions p where (r.visibility <> 2 or (p.user = ?1 and p.type > 0)) and r.code = ?2")
    Optional<Resource> findOneUserWrite(User u, String code);
}

