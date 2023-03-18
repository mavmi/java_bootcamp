package ex04;

import java.util.UUID;

public class TransactionsService {
    private UsersList usersList;

    public TransactionsService(){
        usersList = new UsersArrayList();
    }

    public void addUser(User user){
        usersList.addUser(user);
    }
    public int getBalance(int userId){
        return usersList.getById(userId).getBalance();
    }
    public void doTransfer(int senderId, int recipientId, int transferAmount){
        User sender = usersList.getById(senderId);
        User recipient = usersList.getById(recipientId);
        if (sender.getBalance() < transferAmount){
            throw new IllegalTransactionException("User " + sender.getName() + " doesn't have enough money");
        }
        
        UUID newId = UUID.randomUUID();
        Transaction credit = new Transaction();
        credit.setIdentifier(newId);
        credit.setSender(sender);
        credit.setRecipient(recipient);
        credit.setTransferAmount(-1 * transferAmount);
        credit.setTransferCategory(TRANSFER_CATEGORY.credits);

        Transaction debit = new Transaction();
        debit.setIdentifier(newId);
        debit.setSender(sender);
        debit.setRecipient(recipient);
        debit.setTransferAmount(transferAmount);
        debit.setTransferCategory(TRANSFER_CATEGORY.debits);

        sender.getTransactions().addTransaction(credit);
        recipient.getTransactions().addTransaction(debit);

        sender.setBalance(sender.getBalance() - transferAmount);
        recipient.setBalance(recipient.getBalance() + transferAmount);
    }
    public Transaction[] getTransactions(int userId){
        return usersList.getById(userId).getTransactions().toArray();
    }
    public void removeTransaction(UUID transactionId, int userId){
        usersList.getById(userId).getTransactions().removeById(transactionId);
    }
    public Transaction[] checkValidity(){
        boolean found = false;
        TransactionsList list = new TransactionsLinkedList();

        for (int i = 0; i < usersList.getSize(); i++){
            Transaction[] trAr1 = usersList.getByIndex(i).getTransactions().toArray();
            for (int t1 = 0; t1 < trAr1.length; t1++){
                found = false;
                Transaction transaction1 = trAr1[t1];
                for (int j = 0; j < usersList.getSize(); j++){
                    if (i == j){
                        continue;
                    }
                    Transaction[] trAr2 = usersList.getByIndex(j).getTransactions().toArray();
                    for (int t2 = 0; t2 < trAr2.length; t2++){
                        Transaction transaction2 = trAr2[t2];
                        if (transaction1.getIdentifier().equals(transaction2.getIdentifier())){
                            found = true;
                            break;
                        }
                    }
                    if (found){
                        break;
                    }
                }
                if (!found){
                    list.addTransaction(transaction1);
                }
            }
        }

        return list.toArray();
    }

}
