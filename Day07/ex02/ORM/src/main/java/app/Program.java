package app;

import model.User;

public class Program {
    private final static String PACKAGE_NAME = "model";

    public static void main(String[] args){
        try {
            OrmManager ormManager;

            {
                ormManager = new OrmManager();
                ormManager.createTables();
            }
            {
                User user1 = new User(1L, "name1", "surname1", 1);
                User user2 = new User(2L, "name2", "surname2", 2);
                User user3 = new User(3L, "name3", "surname3", 3);

                ormManager.save(user1);
                ormManager.save(user2);
                ormManager.save(user3);
            }
            {
                User user = new User(2L, "name22", "surname22", 322);
                ormManager.update(user);
            }
            {
                User user = ormManager.findById(2L, User.class);
                System.out.println(user);
            }
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
