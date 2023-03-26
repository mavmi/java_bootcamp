package school21.spring.service.repositories;

import school21.spring.service.models.User;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private DataSource dataSource;

    public UsersRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                "select * from \"user\" where id = ?;"
            );
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return Optional.empty();
            return Optional.of(new User(
                    resultSet.getLong("id"),
                    resultSet.getString("email")
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            List<User> users = new ArrayList<>();
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                    "select * from \"user\";"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                users.add(
                    new User(
                            resultSet.getLong("id"),
                            resultSet.getString("email")
                    )
                );
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User entity) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                    "insert into \"user\" values(?, ?);"
            );
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User entity) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                    "update \"user\" set email = ? where id = ?;"
            );
            preparedStatement.setString(1, entity.getEmail());
            preparedStatement.setLong(2, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                    "delete from \"user\" where id = ?;"
            );
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                    "select * from \"user\" where email = ?;"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return Optional.empty();
            return Optional.of(new User(
                    resultSet.getLong("id"),
                    resultSet.getString("email")
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
