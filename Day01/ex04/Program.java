package ex04;

import java.util.UUID;

public class Program {
    public static void main(String[] args){
        User user0 = new User();
        User user1 = new User();
        User user2 = new User();
        user0.setName("User0");
        user0.setBalance(1000);
        user1.setName("User1");
        user1.setBalance(100);
        user2.setName("User2");
        user2.setBalance(0);

        TransactionsService transactionService = new TransactionsService();
        try{
            transactionService.getBalance(0);
            check(false);
        } catch (UserNotFoundException e){}
        try{
            transactionService.getBalance(1);
            check(false);
        } catch (UserNotFoundException e){}
        try{
            transactionService.getBalance(2);
            check(false);
        } catch (UserNotFoundException e){}

        transactionService.addUser(user0);
        transactionService.addUser(user1);
        transactionService.addUser(user2);
        try{
            check(transactionService.getBalance(0) == 1000);
            check(transactionService.getBalance(1) == 100);
            check(transactionService.getBalance(2) == 0);

            check(transactionService.getTransactions(0).length == 0);
            check(transactionService.getTransactions(1).length == 0);
            check(transactionService.getTransactions(2).length == 0);
        } catch (UserNotFoundException e){
            check(false);
        }
        try{
            transactionService.getBalance(3);
            check(false);
        } catch (UserNotFoundException e){}

        check(transactionService.checkValidity().length == 0);
        try {
            transactionService.doTransfer(user1.getIdentifier(), user2.getIdentifier(), 200);
            check(false);
        } catch (IllegalTransactionException e){}

        transactionService.doTransfer(user1.getIdentifier(), user2.getIdentifier(), 50);
        check(user1.getBalance() == 50);
        check(user2.getBalance() == 50);
        check(user1.getTransactions().toArray().length == 1);
        check(user2.getTransactions().toArray().length == 1);
        check(transactionService.checkValidity().length == 0);

        transactionService.doTransfer(user0.getIdentifier(), user2.getIdentifier(), 500);
        check(user0.getBalance() == 500);
        check(user2.getBalance() == 550);
        check(user0.getTransactions().toArray().length == 1);
        check(user2.getTransactions().toArray().length == 2);
        check(transactionService.checkValidity().length == 0);

        transactionService.doTransfer(user1.getIdentifier(), user0.getIdentifier(), 12);
        check(user0.getBalance() == 512);
        check(user1.getBalance() == 38);
        check(user0.getTransactions().toArray().length == 2);
        check(user1.getTransactions().toArray().length == 2);
        check(transactionService.checkValidity().length == 0);

        Transaction unpaired = new Transaction();
        final UUID unpairedId = UUID.fromString("123e4567-e89b-42d3-a456-556642440000"); 
        unpaired.setIdentifier(unpairedId);
        unpaired.setRecipient(user1);
        unpaired.setSender(new User());
        unpaired.setTransferAmount(123);
        unpaired.setTransferCategory(TRANSFER_CATEGORY.credits);
        user1.getTransactions().addTransaction(unpaired);
        Transaction[] arr = transactionService.checkValidity();
        check(arr.length == 1);
        check(arr[0].getIdentifier().equals(unpairedId));
    }

    private static void check(boolean state){
        if (!state){
            System.out.println("Assertion failed!");
            throw new RuntimeException();
        }
    }
}
