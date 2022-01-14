package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, String>
{
    @Query("select m from Message m where m.index between ?1 * ?2 and ?1 * ?2 + ?2")
    Set<Message> findAll(int page, int limit);
}
