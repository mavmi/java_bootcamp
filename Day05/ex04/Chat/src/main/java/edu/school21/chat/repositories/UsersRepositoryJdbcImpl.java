package edu.school21.chat.repositories;

import edu.school21.chat.models.ChatRoom;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersRepositoryJdbcImpl implements UsersRepository{
    private DataSource dataSource;

    public UsersRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll(int page, int size) {
        Map<Long, User> idToUser = new HashMap<>();
        Map<Long, ChatRoom> idToRoom = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("with userToOwnRooms as (")
                .append("select ")
                .append("\"user\".id as user_id, ")
                .append("\"user\".login as user_login, ")
                .append("\"user\".password as user_passwd, ")
                .append("chatroom.id as own_room_id, ")
                .append("chatroom.name as own_room_name ")
                .append("from \"user\" ")
                .append("full join chatroom ")
                .append("on \"user\".id = chatroom.owner ")
                .append("), userToAllRooms as (")
                .append("select ")
                .append("userToOwnRooms.user_id, ")
                .append("userToOwnRooms.user_login, ")
                .append("userToOwnRooms.user_passwd, ")
                .append("userToOwnRooms.own_room_id, ")
                .append("userToOwnRooms.own_room_name, ")
                .append("chatroom.id as part_of_room_id, ")
                .append("chatroom.name as part_of_room_name ")
                .append("from userToOwnRooms ")
                .append("full join message ")
                .append("on userToOwnRooms.user_id = message.author ")
                .append("full join chatroom ")
                .append("on message.room = chatroom.id ")
                .append(") ")
                .append("select * from userToAllRooms ")
                .append("where user_id is not null;");

        try {
            if (page < 0 || size < 1) throw new SQLException("Invalid arguments");

            ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery(query.toString());
            while (resultSet.next()){
                long user_id = resultSet.getLong("user_id");
                String user_login = resultSet.getString("user_login");
                String user_passwd = resultSet.getString("user_passwd");
                long own_room_id = resultSet.getLong("own_room_id");
                String own_room_name = resultSet.getString("own_room_name");
                long part_room_id = resultSet.getLong("part_of_room_id");
                String part_room_name = resultSet.getString("part_of_room_name");

                User user = idToUser.get(user_id);
                if (user == null){
                   user = new User(user_id, user_login, user_passwd, new ArrayList<>(), new ArrayList<>());
                   idToUser.put(user_id, user);
                }

                ChatRoom ownRoom = idToRoom.get(own_room_id);
                if (own_room_id != 0) {
                    if (ownRoom == null) {
                        ownRoom = new ChatRoom(own_room_id, own_room_name, user, new ArrayList<>());
                        idToRoom.put(own_room_id, ownRoom);
                    } else if (ownRoom.getOwner() == null) {
                        ownRoom.setOwner(user);
                    }
                }

                ChatRoom partOfRoom = idToRoom.get(part_room_id);
                if (part_room_id != 0) {
                    if (partOfRoom == null) {
                        partOfRoom = new ChatRoom(part_room_id, part_room_name, null, new ArrayList<>());
                        idToRoom.put(part_room_id, partOfRoom);
                    }
                }

                List<ChatRoom> createdRooms = user.getCreatedRooms();
                List<ChatRoom> rooms = user.getRooms();
                if (ownRoom != null){
                    if (createdRooms.size() > 0) {
                        for (int i = 0; i < createdRooms.size(); i++) {
                            if (createdRooms.get(i).getId() == own_room_id) break;
                            if (i + 1 == createdRooms.size()) createdRooms.add(ownRoom);
                        }
                    } else {
                        createdRooms.add(ownRoom);
                    }
                }

                if (ownRoom != null) {
                    if (rooms.size() > 0) {
                        for (int i = 0; i < rooms.size(); i++) {
                            if (rooms.get(i).getId() == own_room_id) break;
                            if (i + 1 == rooms.size()) rooms.add(ownRoom);
                        }
                    } else {
                        rooms.add(ownRoom);
                    }
                }

                if (partOfRoom != null){
                    if (rooms.size() > 0) {
                        for (int i = 0; i < rooms.size(); i++) {
                            if (rooms.get(i).getId() == part_room_id) break;
                            if (i + 1 == rooms.size()) rooms.add(partOfRoom);
                        }
                    } else {
                        rooms.add(partOfRoom);
                    }
                }
            }

            List<User> result = new ArrayList<>();
            for (long i = page * size + 1; i <= (page + 1) * size; i++){
                User user = idToUser.get(i);
                if (user != null) result.add(user);
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
