package edu.school21.chat.models;

import java.util.Calendar;

public class Message {
    private long id;
    private User author;
    private ChatRoom room;
    private String text;
    private Calendar datetime;

    public Message(long id, User author, ChatRoom room, String text, Calendar datetime){
        this.id = id;
        this.author = author;
        this.room = room;
        this.text = text;
        this.datetime = datetime;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }

    public void setAuthor(User author){
        this.author = author;
    }
    public User getAuthor(){
        return author;
    }

    public void setRoom(ChatRoom room){
        this.room = room;
    }
    public ChatRoom getRoom(){
        return room;
    }

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return text;
    }

    public void setDatetime(Calendar datetime){
        this.datetime = datetime;
    }
    public Calendar getDatetime(){
        return datetime;
    }

    @Override
    public int hashCode() {
        return
                (int)id +
                author.hashCode() +
                room.hashCode() +
                text.hashCode() +
                datetime.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Message)) return false;
        Message obj1 = (Message)obj;
        if (this.id != obj1.id) return false;
        if (!this.author.equals(obj1.author)) return false;
        if (!this.room.equals(obj1.room)) return false;
        if (!this.text.equals(obj1.text)) return false;
        if (!this.datetime.equals(obj1.datetime)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(
                "id: " + Long.toString(id) + "\n" +
                "author: " + author.getLogin() + "\n" +
                "room: " + room.getName() + "\n" +
                "text: " + text + "\n" +
                "datetime: " + datetime.toString() + "\n"
        );
        return result.toString();
    }
}
