package ex05;

public interface UsersList {
    public void addUser(User user);
    public User getById(int id);
    public User getByIndex(int index);
    public int getSize();
}
