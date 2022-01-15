package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.Resource;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResourceRepository extends PagingAndSortingRepository<Resource, String> {}

