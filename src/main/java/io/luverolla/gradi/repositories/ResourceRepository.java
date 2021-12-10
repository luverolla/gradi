package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.Resource;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {}

