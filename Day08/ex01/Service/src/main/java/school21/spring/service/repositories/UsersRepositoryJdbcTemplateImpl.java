package school21.spring.service.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import school21.spring.service.models.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> rowMapper;

    public UsersRepositoryJdbcTemplateImpl(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        rowMapper = (rs, rowNum) -> {
            return new User(rs.getLong("id"), rs.getString("email"));
        };
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
        jdbcTemplate.update("insert into \"user\" values(?, ?);", entity.getId(), entity.getEmail());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("update \"user\" set email = ? where id = ?;", entity.getEmail(), entity.getId());
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
