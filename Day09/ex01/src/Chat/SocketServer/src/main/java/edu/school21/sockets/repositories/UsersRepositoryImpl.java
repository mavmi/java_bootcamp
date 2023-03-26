package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component("usersRepositoryJdbcTemplate")
public class UsersRepositoryImpl implements UsersRepository{
    @Autowired
    @Qualifier("HikariDataSource")
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> rowMapper;

    public UsersRepositoryImpl(){
        rowMapper = (rs, rowNum) -> {
            return new User(rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getBoolean("log")
            );
        };
    }

    @PostConstruct
    public void init(){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query("select * from \"user\" where id = ?;", rowMapper, id);
        if (users.size() == 0) return Optional.empty();
        else return Optional.of(users.get(0));
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from \"user\";", rowMapper);
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.update("insert into \"user\"(email, password, log) values(?, ?, ?);", entity.getEmail(), entity.getPassword(), entity.getLog());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("update \"user\" set email = ?, password = ?, log = ? where id = ?;", entity.getEmail(), entity.getPassword(), entity.getLog(), entity.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from \"user\" where id = ?;", id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query("select * from \"user\" where email = ?;", rowMapper, email);
        if (users.size() == 0) return Optional.empty();
        else return Optional.of(users.get(0));
    }
}
