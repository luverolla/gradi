package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, String>
{
    @Query("select u from User u where u.index between ?1 * ?2 and ?1 * ?2 + ?2")
    Set<User> findAll(int page, int limit);

    @Query("select u from User u where u.role = ?1")
    Set<User> findAllByRole(User.Role role);
}
