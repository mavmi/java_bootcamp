package ex05;

import java.util.UUID;

public interface TransactionsList {
    public void addTransaction(Transaction transaction);
    public Transaction removeById(UUID id);
    public Transaction[] toArray();
}
