package ex02;

import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        int coffeeCount = 0;
        Scanner scanner = new Scanner(System.in);

        while (true){
            int input = scanner.nextInt();
            if (input == 42) break;
            if (input == 0 || input == 1) continue;
            if (isPrime(sumDigits(input))) coffeeCount++;
        }

        System.out.println("Count of coffee-request – " + coffeeCount);
    }

    private static int sumDigits(int inputNumber){
        int sum = 0;

        while (inputNumber > 0){
            sum += inputNumber % 10;
            inputNumber /= 10;
        }

        return sum;
    }
    private static boolean isPrime(int inputNumber){
        if (inputNumber <= 1){
            return false;
        } else if (inputNumber == 2) {
            return true;
        } else {
            int i;
            for (i = 2; i * i <= inputNumber; i++){
                if (inputNumber % i != 0) continue;
                return false;
            }
            return true;
        }
    }
}
