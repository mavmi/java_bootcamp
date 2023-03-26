package edu.school21.models;

public class User {
    private long id;
    private String login;
    private String password;
    private boolean auth_status;

    public User(long id, String login, String password, boolean auth_status){
        this.id = id;
        this.login = login;
        this.password = password;
        this.auth_status = auth_status;
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

    public void setAuthStatus(boolean auth_status){
        this.auth_status = auth_status;
    }
    public boolean getAuthStatus(){
        return auth_status;
    }

    @Override
    public int hashCode() {
        return
                (int)id +
                login.hashCode() +
                password.hashCode() +
                ((auth_status) ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User obj1 = (User)obj;
        if (this.id != obj1.id) return false;
        if (!this.login.equals(obj1.login)) return false;
        if (!this.password.equals(obj1.password)) return false;
        if (this.auth_status != obj1.auth_status) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("id=")
                .append(id)
                .append(",login=")
                .append("\"")
                .append(login)
                .append("\"")
                .append(",password=")
                .append("\"")
                .append(password)
                .append("\"")
                .append(",auth_status=")
                .append("\"")
                .append(auth_status)
                .append("\"");

        return result.toString();
    }
}
