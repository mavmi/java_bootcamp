package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

import java.util.Optional;

public interface UsersService {
    String signUp(String email, String password);
    User signIn(String email, String password);
    void exit(String email);
    Optional<User> findByEmail(String email);
}
