package ex01;

public class Program {
    public static void main(String[] args){
        for (int i = 0; i < 256; i++){
            User user = new User();
            printOnFalse(user.getIdentifier() == i);
        }
    }

    private static void printOnFalse(boolean statement){
        if (!statement){
            System.out.println("ERROR");
        }
    }
}
