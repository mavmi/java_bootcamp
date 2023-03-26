package ex04;

import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int[] countArray = parseInput(scanner);
        char[] topTenChars = getTopTenChars(countArray);
        printTopTen(countArray, topTenChars);
    }

    private static int[] parseInput(Scanner scanner){
        int[] countArray = new int[65535];
        char[] inputLine = scanner.nextLine().toCharArray();

        for (int i = 0; i < inputLine.length; i++){
            countArray[(int)inputLine[i]]++;
        }

        return countArray;
    }
    private static char[] getTopTenChars(int[] countArray){
        char[] topTenChars = new char[10];

        for (int ca_i = 0; ca_i < countArray.length; ca_i++){

            char ca_symb = (char)ca_i;
            for (int tt_i = 0; tt_i < topTenChars.length; tt_i++){
                char tt_symb = topTenChars[tt_i];

                if (countArray[ca_symb] < countArray[tt_symb]) continue;

                char left, right;
                if (countArray[ca_symb] > countArray[tt_symb]){
                    left = ca_symb;
                    right = tt_symb;
                } else {
                    left = (ca_symb < tt_symb) ? ca_symb : tt_symb;
                    right = (left == ca_symb) ? tt_symb : ca_symb;
                }

                for (int i = topTenChars.length - 2; i > tt_i; i--){
                    topTenChars[i + 1] = topTenChars[i];
                }
                topTenChars[tt_i] = left;
                if (tt_i + 1 != topTenChars.length) {
                    topTenChars[tt_i + 1] = right;
                }

                break;
            }
        }

        return topTenChars;
    }
    private static void printTopTen(int[] countArray, char[] topTenChars){
        int maxHeight;
        int[] topTenHeights = new int[topTenChars.length];
        boolean[] topTenHeaders = new boolean[topTenChars.length];

        for (int i = 0; i < topTenChars.length; i++){
            if (countArray[topTenChars[0]] >= 10){
                topTenHeights[i] = (int)((double)countArray[topTenChars[i]] / (double)countArray[topTenChars[0]] * 10);
            } else {
                topTenHeights[i] = countArray[topTenChars[i]];
            }
            topTenHeights[i]++;
            topTenHeaders[i] = false;
        }
        maxHeight = topTenHeights[0];

        for (int lineNum = maxHeight; lineNum > 0; lineNum--){
            for (int i = 0; i < topTenChars.length; i++){
                if (topTenHeights[i] < lineNum) continue;
                if (countArray[topTenChars[i]] == 0) continue;
                if (!topTenHeaders[i]){
                    topTenHeaders[i] = true;
                    int count = countArray[topTenChars[i]];
                    if (count < 10) System.out.print("  " + count);
                    else if (count < 100) System.out.print(" " + count);
                    else System.out.print(count);
                } else {
                    System.out.print("  #");
                }
            }
            System.out.print("\n");
        }
        for (int i = 0; i < topTenChars.length; i++){
            if (countArray[topTenChars[i]] != 0){
                System.out.print("  " + topTenChars[i]);
            }
        }
        System.out.print("\n");
    }
}
