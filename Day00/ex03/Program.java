package ex03;

import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        long container = 0;

        for (long weekNum = 1; weekNum <= 18; weekNum++){
            String weekStr = scanner.nextLine();
            if (weekStr.equals("42")) break;
            if (!weekStr.equals("Week " + weekNum)){
                theIllegalArgument();
                System.exit(-1);
            }
            container = updateContainer(container, getMin(scanner), weekNum);
        }
        printInfo(container);
    }

    private static void printInfo(long container){
        int weekNum = 1;

        while (container > 0){
            System.out.print("Week " + weekNum++ + " ");
            for (int i = 0; i < container % 10; i++){
                System.out.print("=");
            }
            System.out.println(">");
            container /= 10;
        }
    }
    private static long getMin(Scanner scanner){
        long min = 10;

        for (int i = 0; i < 5; i++){
            long tmp = scanner.nextLong();
            if (tmp < min) min = tmp;
        }
        scanner.nextLine();

        return min;
    }
    private static long updateContainer(long container, long num, long pos){
        return container + num * pow(10, pos - 1);
    }
    private static long pow(long base, long exp){
        long result = 1;

        for (long i = 0; i < exp; i++){
            result *= base;
        }

        return result;
    }
    private static void theIllegalArgument(){
        System.err.println("Illegal Argument");
    }
}
