package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.ResourceType;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResourceTypeRepository extends PagingAndSortingRepository<ResourceType, String> {}