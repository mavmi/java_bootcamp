package edu.school21.sockets.models;

public class User {
    private long id;
    private String email;
    private String password;

    public User(){

    }
    public User(long id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    @Override
    public String toString() {
        return
                "[\n" +
                "\tid=" +
                id +
                ",\n" +
                "\temail=" +
                email +
                ",\n" +
                "\tpassword=" +
                password +
                "\n]";
    }
}
