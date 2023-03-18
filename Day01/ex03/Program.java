package ex03;

import java.util.UUID;

public class Program {
    public static void main(String[] args){
        User sender = new User();
        User recipient = new User();
        UUID nonExisting = UUID.fromString("123e4567-e89b-42d3-a456-556642440000");
        UUID[] idx = new UUID[5];
        for (int i = 0; i < idx.length;){
            UUID newId = UUID.randomUUID();
            if (!newId.equals(nonExisting)){
                idx[i++] = newId;
            }
        }

        TransactionsLinkedList list = new TransactionsLinkedList();
        check(list.toArray().length == 0);
        for (int i = 0; i < idx.length; i++){
            Transaction newTransaction = new Transaction();
            newTransaction.setIdentifier(idx[i]);
            newTransaction.setRecipient(recipient);
            newTransaction.setSender(sender);
            newTransaction.setTransferAmount(100 + i);
            newTransaction.setTransferCategory((i % 2 == 0) ? TRANSFER_CATEGORY.debits : TRANSFER_CATEGORY.credits);

            list.addTransaction(newTransaction);
            Transaction[] array = list.toArray();
            check(array.length == i + 1);
            for (int j = 0; j < array.length; j++){
                Transaction tr = array[j];
                check(tr.getIdentifier().equals(idx[j]));
                check(tr.getRecipient() == recipient);
                check(tr.getSender() == sender);
                check(tr.getTransferAmount() == 100 + j);
                check(tr.getTransferCategory() == ((j % 2 == 0) ? TRANSFER_CATEGORY.debits : TRANSFER_CATEGORY.credits));
            }
        }

        try{
            list.removeById(nonExisting);
            check(false);
        } catch (TransactionNotFoundException e){

        }

        Transaction[] array = null;

        list.removeById(idx[4]);
        array = list.toArray();
        check(array.length == 4);
        for (int i = 0; i < array.length; i++){
            check(array[i].getIdentifier().equals(idx[i]));
        }

        list.removeById(idx[0]);
        array = list.toArray();
        check(array.length == 3);
        for (int ar = 0, id = 1; ar < array.length; ar++, id++){
            check(array[ar].getIdentifier().equals(idx[id]));
        }

        list.removeById(idx[2]);
        array = list.toArray();
        check(array.length == 2);
        for (int ar = 0, id = 1; ar < array.length; ar++, id += 2){
            check(array[ar].getIdentifier().equals(idx[id]));
        }

        list.removeById(idx[1]);
        list.removeById(idx[3]);
        check(list.toArray().length == 0);
    }

    private static void check(boolean state){
        if (!state){
            System.out.println("Assertion failed!");
            throw new RuntimeException();
        }
    }
}
