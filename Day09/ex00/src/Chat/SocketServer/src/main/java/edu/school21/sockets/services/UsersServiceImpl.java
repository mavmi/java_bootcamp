package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        usersRepository.save(new User(0L, email, encodedPasswd));
        return encodedPasswd;
    }

    private String generatePassword(){
        char[] pass = new char[12];
        for (int i = 0; i < pass.length; i++){
            char newChar;
            if ((int)(Math.random() * 100) % 2 == 0) {
                newChar = (char)('A' + (int)(Math.random() * 26));
            } else {
                newChar = (char)('a' + (int)(Math.random() * 26));
            }
            pass[i] = newChar;
        }
        return new String(pass);
    }
}

