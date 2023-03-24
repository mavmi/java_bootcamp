package school21.spring.service.models;

public class User {
    private long id;
    private String email;

    public User(){

    }
    public User(long id, String email){
        this.id = id;
        this.email = email;
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

    @Override
    public String toString() {
        return
                "[\n" +
                "\tid=" +
                id +
                ",\n" +
                "\temail=" +
                email +
                "\n]";
    }
}
