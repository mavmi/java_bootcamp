package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.repositories.UsersRepository;
import edu.school21.models.User;

public class UsersServiceImpl {
    private UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public boolean authenticate(String login, String password){
        try {
            User user = usersRepository.findByLogin(login);
            if (user.getAuthStatus()) throw new AlreadyAuthenticatedException();
            if (!user.getPassword().equals(password)) return false;
            user.setAuthStatus(true);
            usersRepository.update(user);
            return true;
        } catch (EntityNotFoundException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
