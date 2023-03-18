package ex04;

public class UserIdsGenerator {
    private static UserIdsGenerator userIdsGenerator = null;
    private int lastId = -1;

    public static UserIdsGenerator getInstance(){
        if (userIdsGenerator == null){
            userIdsGenerator = new UserIdsGenerator();
        }
        return userIdsGenerator;
    }
    public int generateId(){
        return ++lastId;
    }

    private UserIdsGenerator(){

    } 
}
