package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component("messagesRepositoryJdbcImpl")
public class MessagesRepositoryJdbcImpl implements MessagesRepository{
    @Autowired
    @Qualifier("HikariDataSource")
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Message> rowMapper;

    public MessagesRepositoryJdbcImpl(){
        rowMapper = (rs, rowNum) -> {
            return new Message(
                    rs.getLong("id"),
                    new User(rs.getLong("author"), null, null, false),
                    rs.getString("text"),
                    rs.getTimestamp("datetime")
            );
        };
    }

    @PostConstruct
    public void init(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Message> findById(Long id) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("select ")
                    .append("message.id as m_id, ")
                    .append("message.text as m_text, ")
                    .append("message.datetime as m_datetime, ")
                    .append("\"user\".id as u_id, ")
                    .append("\"user\".email as u_email, ")
                    .append("\"user\".password as u_password, ")
                    .append("from message ")
                    .append("left join \"user\" on message.author = \"user\".id ")
                    .append("where message.id = ")
                    .append(id)
                    .append(";");

            ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery(query.toString());
            if (!resultSet.next()) throw new RuntimeException("Query result is empty");

            User user = new User(
                    resultSet.getLong("u_id"),
                    resultSet.getString("u_login"),
                    resultSet.getString("u_password"),
                    false
            );
            Message message = new Message(
                    resultSet.getLong("m_id"),
                    user,
                    resultSet.getString("m_text"),
                    resultSet.getTimestamp("m_datetime")
            );

            return Optional.of(message);

        } catch (RuntimeException | SQLException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void save(Message message) {
        try {
            jdbcTemplate.update(
                    "insert into message(author, text, datetime) values(?, ?, ?);",
                    message.getAuthor().getId(),
                    message.getText(),
                    message.getDatetime()
            );
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                    "select id from message where datetime = ?;"
            );
            preparedStatement.setTimestamp(1, message.getDatetime());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()){
                message.setId(resultSet.getLong("id"));
            } else {
                throw new RuntimeException("New row was is not added");
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Message message) {
        jdbcTemplate.update(
                "update message set author = ?, text = ?, datetime = ? where id = ?;",
                message.getAuthor().getId(),
                message.getText(),
                message.getDatetime(),
                message.getId()
        );
    }
}
