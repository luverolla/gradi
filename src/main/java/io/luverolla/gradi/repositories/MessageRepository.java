package io.luverolla.gradi.repositories;

import io.luverolla.gradi.entities.Message;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<Message, String> {}
