package ex02;

public class Program {
    public static void main(String[] args){
        UsersArrayList usersArrayList = new UsersArrayList();
        printOnFalse(usersArrayList.getSize() == 0);
        
        for (int i = 1; i <= 5; i++){
            usersArrayList.addUser(createNewUser(i));
        }

        printOnFalse(usersArrayList.getSize() == 5);
        for (int i = 1; i < usersArrayList.getSize(); i++){
            User user = usersArrayList.getByIndex(i - 1);
            printOnFalse(user.getIdentifier() == i - 1);
            printOnFalse(user.getBalance() == 100 + i);
            printOnFalse(user.getName().equals("User_" + i));
        }

        for (int i = 6; i <= 15; i++){
            usersArrayList.addUser(createNewUser(i));
        }
        
        printOnFalse(usersArrayList.getSize() == 15);
        for (int i = 0; i < usersArrayList.getSize(); i++){
            try {
                usersArrayList.getById(i);
            } catch (UserNotFoundException e){
                printOnFalse(false);
            }
        }
        try {
            usersArrayList.getById(usersArrayList.getSize());
            printOnFalse(false);
        } catch (UserNotFoundException e){
            
        }
        printOnFalse(usersArrayList.getByIndex(usersArrayList.getSize()) == null);
        printOnFalse(usersArrayList.getByIndex(-1) == null);
    }

    private static User createNewUser(int num){
        User user = new User();
        user.setName("User_" + num);
        user.setBalance(100 + num);
        return user;
    }
    private static void printOnFalse(boolean statement){
        if (!statement){
            System.out.println("ERROR");
        }
    }
}
