package edu.school21.chat.repositories;

import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UsersRepositoryJdbcImpl implements UsersRepository{
    private DataSource dataSource;

    public UsersRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll(int page, int size) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("select * from \"user\" where id >= (")
                    .append(page)
                    .append(" * ")
                    .append(size)
                    .append(") + 1 and id <= ((")
                    .append(page)
                    .append(" + 1) * ")
                    .append(size)
                    .append(");");

            ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery(query.toString());
            while (resultSet.next()){
                System.out.println(
                        "id: " + resultSet.getLong("id") + "; " +
                        "login: " + resultSet.getString("login") + "; " +
                        "password: " + resultSet.getString("password")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
