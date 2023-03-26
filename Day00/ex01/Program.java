package ex01;

import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        int inputNumber = new Scanner(System.in).nextInt();

        if (inputNumber <= 1){
            theIllegalArgument();
            System.exit(-1);
        } else if (inputNumber == 2) {
            System.out.println("true 1");
        } else {
            int i;
            for (i = 2; i * i <= inputNumber; i++){
                if (inputNumber % i != 0) continue;
                System.out.println("false " + --i);
                System.exit(0);
            }
            System.out.println("true " + --i);
        }
    }

    private static void theIllegalArgument(){
        System.err.println("Illegal Argument");
    }
}
