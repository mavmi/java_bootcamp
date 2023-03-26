package ex02;

public class Program{
    public static void main(String[] args){
        int arraySize = 0;
        int threadsCount = 0;
        try {
            int[] parsedArgs = parseArgs(args);
            arraySize = parsedArgs[0];
            threadsCount = parsedArgs[1];
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        int[] numsArray = generateArrayOfNums(arraySize);
        Calculator[] threadsArray = generateArrayOfThreads(threadsCount, numsArray, arraySize);
        printArraySum(numsArray, arraySize);
        
        for (int i = 0; i < threadsCount; i++) threadsArray[i].start();
        for (int i = 0; i < threadsCount; i++){
            try {
                threadsArray[i].join();
            } catch (InterruptedException e){}
        }

        System.out.println("Sum by threads: " + threadsArray[0].getTotalSum());
    }

    private static void printArraySum(int[] array, int arraySize){
        int sum = 0;
        for (int i = 0; i < arraySize; i++) sum += array[i];
        System.out.println("Sum: " + sum);
    }
    private static Calculator[] generateArrayOfThreads(int len, int[] numsArray, int numsArraySize){
        if (len == 1){
            return new Calculator[]{
                new Calculator(numsArray, 0, numsArraySize)
            };
        }

        int offset = 0;
        int delta = numsArraySize / len;
        int lastDelta = numsArraySize - delta * (len - 1);
        Calculator[] array = new Calculator[len];

        for (int i = 0; i < len - 1; i++){
            array[i] = new Calculator(numsArray, offset, delta);
            offset += delta;
        }
        array[len - 1] = new Calculator(numsArray, offset, lastDelta);

        return array;
    }
    private static int[] generateArrayOfNums(int len){
        int[] array = new int[len];
        for (int i = 0; i < len; i++) array[i] = (int)(Math.random() * 1000);
        return array;
    }

    private static int[] parseArgs(String[] args){
        final String errMsg = "Error\n" +
                                "Usage: Program.java --arraySize=[COUNT_OF_NUMBERS] --threadsCount=[NUMBER_OF_THREADS]";
        
        if (args.length != 2) throw new RuntimeException(errMsg);

        int[] arr = new int[]{
            parseArg(args[0], "--arraySize"),
            parseArg(args[1], "--threadsCount")
        };
        if (arr[0] > 2000000) throw new RuntimeException("Maximum number of array elements is 2,000,000");
        if (arr[1] > arr[0]) throw new RuntimeException("Maximum number of threads is no greater than current number of array elements");
        return arr;
    }
    private static int parseArg(String arg, String awaitingKey){
        int eqPos = arg.indexOf('=');
        if (eqPos == -1) throw new RuntimeException();

        String key = arg.substring(0, eqPos);
        String value = arg.substring(eqPos + 1);
        if (!key.equals(awaitingKey)) throw new RuntimeException();
        try {
            int count = Integer.parseInt(value);
            if (count > 0) return count;
            else throw new NumberFormatException("Arguments must be positive numbers");
        } catch (NumberFormatException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}