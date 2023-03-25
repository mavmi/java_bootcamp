package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.MessagesRepositoryJdbcImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("messageServiceImpl")
public class MessageServiceImpl implements MessageService{
    @Autowired
    @Qualifier("messagesRepositoryJdbcImpl")
    private MessagesRepositoryJdbcImpl messagesRepositoryJdbc;

    public MessageServiceImpl(){

    }

    @Override
    public void save(Message message) {
        messagesRepositoryJdbc.save(message);
    }
}
