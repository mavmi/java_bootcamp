package ex00;

import java.util.UUID;

public class Program{
    public static void main(String[] args){
        User Sender = new User();
        User Recipient = new User();
        Transaction transaction = new Transaction();

        Sender.setName("SENDER");
        printOnFalse(Sender.getName().equals("SENDER"));
        Sender.setIdentifier(123);
        printOnFalse(Sender.getIdentifier() == 123);
        Sender.setBalance(-2);
        printOnFalse(Sender.getBalance() == 0);
        Sender.setBalance(0);
        printOnFalse(Sender.getBalance() == 0);
        Sender.setBalance(1000);
        printOnFalse(Sender.getBalance() == 1000);

        Recipient.setName("RECIPIENT");
        printOnFalse(Recipient.getName().equals("RECIPIENT"));
        Recipient.setIdentifier(111);
        printOnFalse(Recipient.getIdentifier() == 111);
        Recipient.setBalance(5000);
        printOnFalse(Recipient.getBalance() == 5000);

        transaction.setIdentifier(UUID.randomUUID());
        transaction.setRecipient(Recipient);
        printOnFalse(transaction.getRecipient() == Recipient);
        transaction.setSender(Sender);
        printOnFalse(transaction.getSender() == Sender);
        transaction.setTransferCategory(TRANSFER_CATEGORY.credits);
        printOnFalse(transaction.getTransferCategory() == TRANSFER_CATEGORY.credits);
        transaction.setTransferAmount(500);
        printOnFalse(transaction.getTransferAmount() == 0);
        transaction.setTransferAmount(-500);
        printOnFalse(transaction.getTransferAmount() == -500);
        transaction.setTransferCategory(TRANSFER_CATEGORY.debits);
        printOnFalse(transaction.getTransferCategory() == TRANSFER_CATEGORY.debits);
        transaction.setTransferAmount(500);
        printOnFalse(transaction.getTransferAmount() == 500);
    }

    private static void printOnFalse(boolean statement){
        if (!statement){
            System.out.println("ERROR");
        }
    }
}
