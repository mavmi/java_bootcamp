package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersServiceImplTest {
    private UsersRepository usersRepository;
    private User user1;
    private User user2;

    @BeforeEach
    public void init(){
        user1 = new User(1, "user1", "passwd1", true);
        user2 = new User(2, "user2", "passwd2", false);

        usersRepository = mock(UsersRepository.class);
        when(usersRepository.findByLogin("user1")).thenReturn(new User(1, "user1", "passwd1", true));
        when(usersRepository.findByLogin("user2")).thenReturn(new User(2, "user2", "passwd2", false));
        when(usersRepository.findByLogin("user3")).thenThrow(new EntityNotFoundException());
    }

    @Test
    public void testCorrectPasswd_false(){
        Assertions.assertTrue(new UsersServiceImpl(usersRepository).authenticate(user2.getLogin(), user2.getPassword()));
    }

    @Test
    public void testIncorrectPasswd_false(){
        Assertions.assertFalse(new UsersServiceImpl(usersRepository).authenticate(user2.getLogin(), "qwe123"));
    }

    @Test
    public void testCorrectPasswd_true(){
        Assertions.assertThrows(
                AlreadyAuthenticatedException.class,
                () -> new UsersServiceImpl(usersRepository).authenticate(user1.getLogin(), user1.getPassword())
        );
    }

    @Test
    public void testIncorrectPasswd_true(){
        Assertions.assertThrows(
                AlreadyAuthenticatedException.class,
                () -> new UsersServiceImpl(usersRepository).authenticate(user1.getLogin(), "qwe123")
        );
    }

    @Test
    public void testIncorrectUser(){
        Assertions.assertFalse(new UsersServiceImpl(usersRepository).authenticate("user3", "qwe123"));
    }
}
