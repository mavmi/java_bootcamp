package edu.school21.chat.models;

import java.util.List;

public class ChatRoom {
    private long id;
    private String name;
    private User owner;
    private List<Message> messages;

    public ChatRoom(long id, String name, User owner, List<Message> messages){
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.messages = messages;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }
    public User getOwner(){
        return owner;
    }

    public List<Message> getMessages(){
        return messages;
    }

    @Override
    public int hashCode() {
        return
                (int)id +
                name.hashCode() +
                owner.hashCode() +
                messages.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChatRoom)) return false;
        ChatRoom obj1 = (ChatRoom)obj;
        if (this.id != obj1.id) return false;
        if (!(this.name.equals(obj1.name))) return false;
        if (!(this.owner.equals(obj1.owner))) return false;
        if (!(this.messages.equals(obj1.messages))) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(
                "id: " + Long.toString(id) + "\n" +
                "name: " + name + "\n" +
                "owner: " + owner.getLogin() + "\n" +
                "messages: ["
        );
        for (int i = 0; i < messages.size(); i++){
            result.append(messages.get(i).getText());
            if (i + 1 != messages.size()) result.append(", ");
        }
        result.append("]\n");
        return result.toString();
    }
}
