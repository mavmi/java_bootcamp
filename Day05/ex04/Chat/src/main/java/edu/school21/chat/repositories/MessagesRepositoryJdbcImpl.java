package edu.school21.chat.repositories;

import edu.school21.chat.models.ChatRoom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private DataSource dataSource;

    public MessagesRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
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
                    .append("\"user\".login as u_login, ")
                    .append("\"user\".password as u_password, ")
                    .append("chatroom.id as r_id, ")
                    .append("chatroom.name as r_name ")
                    .append("from message ")
                    .append("left join \"user\" on message.author = \"user\".id ")
                    .append("left join chatroom on message.room = chatroom.id ")
                    .append("where message.id = ")
                    .append(Long.toString(id))
                    .append(";");

            ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery(query.toString());
            if (!resultSet.next()) throw new RuntimeException("Query result is empty");

            User user = new User(
                    resultSet.getLong("u_id"),
                    resultSet.getString("u_login"),
                    resultSet.getString("u_password"),
                    null,
                    null
            );
            ChatRoom chatRoom = new ChatRoom(
                    resultSet.getLong("r_id"),
                    resultSet.getString("r_name"),
                    null,
                    null
            );
            Message message = new Message(
                    resultSet.getLong("m_id"),
                    user,
                    chatRoom,
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
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                    "insert into message(author, room, text, datetime) values(?, ?, ?, ?);"
            );
            preparedStatement.setLong(1, message.getAuthor().getId());
            preparedStatement.setLong(2, message.getRoom().getId());
            preparedStatement.setString(3, message.getText());
            preparedStatement.setTimestamp(4, message.getDatetime());
            preparedStatement.executeUpdate();

            preparedStatement = dataSource.getConnection().prepareStatement(
                    "select id from message where datetime = ?;"
            );
            preparedStatement.setTimestamp(1, message.getDatetime());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()){
                message.setId(resultSet.getLong("id"));
            } else {
                throw new NotSavedSubEntityException("New row was is not added");
            }
        } catch (RuntimeException | SQLException e) {
            throw new NotSavedSubEntityException(e.getMessage());
        }
    }

    @Override
    public void update(Message message) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                    "update message set author = ?, room = ?, text = ?, datetime = ? where id = ?;"
            );
            preparedStatement.setLong(1, message.getAuthor().getId());
            preparedStatement.setLong(2, message.getRoom().getId());
            preparedStatement.setString(3, message.getText());
            preparedStatement.setTimestamp(4, message.getDatetime());
            preparedStatement.setLong(5, message.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
