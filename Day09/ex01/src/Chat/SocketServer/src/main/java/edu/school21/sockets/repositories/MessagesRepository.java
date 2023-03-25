package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;

import java.util.Optional;

public interface MessagesRepository {
    Optional<Message> findById(Long id);
    void save(Message message);
    void update(Message message);
}


