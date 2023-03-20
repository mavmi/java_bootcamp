package edu.school21.numbers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class NumberWorkerTest {

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53})
    public void isPrimeForPrimes(int number){
        assertTrue(new NumberWorker().isPrime(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27})
    public void isPrimeForNotPrimes(int number){
        assertFalse(new NumberWorker().isPrime(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {-100500, -100, -1, 0, 1})
    public void isPrimeForIncorrectNumbers(int number){
        assertThrows(IllegalNumberException.class, () -> new NumberWorker().isPrime(number));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv")
    public void checkDigitsSum(int number, int sum){
        assertEquals(new NumberWorker().digitsSum(number), sum);
    }
}
