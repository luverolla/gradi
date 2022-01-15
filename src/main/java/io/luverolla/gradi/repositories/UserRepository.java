package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.User;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String> {}
