package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
