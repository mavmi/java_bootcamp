package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Component("usersService")
public class UsersServiceImpl implements UsersService{
    @Autowired
    @Qualifier("usersRepositoryJdbcTemplate")
    private UsersRepository usersRepository;

    @Autowired
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    public UsersServiceImpl(){

    }

    @Override
    public String signUp(String email, String password) {
        if (usersRepository.findByEmail(email).isPresent()) return null;
        String encodedPasswd = passwordEncoder.encode(password);
        usersRepository.save(new User(0L, email, encodedPasswd, false));
        return encodedPasswd;
    }

    @Override
    public User signIn(String email, String password){
        Optional<User> user = usersRepository.findByEmail(email);
        if (!user.isPresent()) return null;
        User u = user.get();
        if (u.getLog()) return null;
        if (!passwordEncoder.matches(password, u.getPassword())) return null;
        u.setLog(true);
        usersRepository.update(u);
        return u;
    }

    @Override
    public void exit(String email){
        Optional<User> user = usersRepository.findByEmail(email);
        if (!user.isPresent()) return;
        User u = user.get();
        u.setLog(false);
        usersRepository.update(u);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
}

