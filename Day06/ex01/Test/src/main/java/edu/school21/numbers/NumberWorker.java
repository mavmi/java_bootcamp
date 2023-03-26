package edu.school21.numbers;

public class NumberWorker {
    public boolean isPrime(int number){
        if (number <= 1){
            throw new IllegalNumberException("Number is less than 2");
        } else if (number == 2) {
            return true;
        } else {
            int i;
            for (i = 2; i * i <= number; i++){
                if (number % i != 0) continue;
                return false;
            }
            return true;
        }
    }
    public int digitsSum(int number){
        long num = (long)number;
        if (num < 0) num *= -1;

        int sum = 0;
        while (num > 0){
            sum += num % 10;
            num /= 10;
        }

        return sum;
    }
}
