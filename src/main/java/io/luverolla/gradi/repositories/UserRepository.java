package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String>
{
    @Query("select u from User u where u.code = '0000000000'")
    User getAdmin();

    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.role = 1")
    Iterable<User> findAllEditors();
}
