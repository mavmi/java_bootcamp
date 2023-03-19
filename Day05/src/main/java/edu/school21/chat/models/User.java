package edu.school21.chat.models;

import java.util.List;

public class User {
    private long id;
    private String login;
    private String password;
    private List<ChatRoom> createdRooms;
    private List<ChatRoom> rooms;

    public User(long id, String login, String password, List<ChatRoom> createdRooms, List<ChatRoom> rooms){
        this.id = id;
        this.login = login;
        this.password = password;
        this.createdRooms = createdRooms;
        this.rooms = rooms;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }

    public void setLogin(String login){
        this.login = login;
    }
    public String getLogin(){
        return login;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    public List<ChatRoom> getCreatedRooms(){
        return createdRooms;
    }

    public List<ChatRoom> getRooms() {
        return rooms;
    }

    @Override
    public int hashCode() {
        return
                (int)id +
                login.hashCode() +
                password.hashCode() +
                createdRooms.hashCode() +
                rooms.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User obj1 = (User)obj;
        if (this.id != obj1.id) return false;
        if (!this.login.equals(obj1.login)) return false;
        if (!this.password.equals(obj1.password)) return false;
        if (!this.createdRooms.equals(obj1.createdRooms)) return false;
        if (!this.rooms.equals(obj1.rooms)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(
                "id: " + Long.toString(id) + "\n" +
                "login: " + login + "\n" +
                "password: " + password + "\n" +
                "created rooms: ["
        );
        for (int i = 0; i < createdRooms.size(); i++){
            result.append(createdRooms.get(i).getName());
            if (i + 1 != createdRooms.size()) result.append(", ");
        }
        result.append("]\n");
        result.append("rooms: [");
        for (int i = 0; i < rooms.size(); i++){
            result.append(rooms.get(i).getName());
            if (i + 1 != rooms.size()) result.append(", ");
        }
        result.append("]\n");
        return result.toString();
    }
}
