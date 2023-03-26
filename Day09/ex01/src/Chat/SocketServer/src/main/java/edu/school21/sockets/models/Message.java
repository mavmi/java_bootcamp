package edu.school21.sockets.models;

import java.sql.Timestamp;

public class Message {
    private long id;
    private User author;
    private String text;
    private Timestamp datetime;

    public Message(long id, User author, String text, Timestamp datetime){
        this.id = id;
        this.author = author;
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

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return text;
    }

    public void setDatetime(Timestamp datetime){
        this.datetime = datetime;
    }
    public Timestamp getDatetime(){
        return datetime;
    }

    @Override
    public int hashCode() {
        return
                (int)id +
                author.hashCode() +
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
        if (!this.text.equals(obj1.text)) return false;
        if (!this.datetime.equals(obj1.datetime)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Message : {\n")
                .append("\tid=")
                .append(id)
                .append(",\n")
                .append("\tauthor={")
                .append(author.toString())
                .append("},\n")
                .append("\ttext=")
                .append("\"")
                .append(text)
                .append("\"")
                .append(",\n")
                .append("\tdateTime=")
                .append(datetime.toString())
                .append(",\n}");
        return result.toString();
    }
}
