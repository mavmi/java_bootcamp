package ex02;

public class Calculator extends Thread{
    private static int sum = 0;
    private int[] array;
    private int offset;
    private int len;
    private int id;
    private static int lastId = 1;

    public Calculator(int[] array, int offset, int len){
        this.array = array;
        this.offset = offset;
        this.len = len;
        id = lastId++;
    }

    public void run(){
        int localSum = 0;
        for (int i = offset; i < offset + len; i++) localSum += array[i];
        appendSum(localSum);
    }

    public int getTotalSum(){
        return sum;
    }

    synchronized private void appendSum(int localSum){
        System.out.println("Thread " + id + ": from " + (offset + 1) + " to " + (offset + len) + " sum is " + localSum);
        sum += localSum;
    }
}