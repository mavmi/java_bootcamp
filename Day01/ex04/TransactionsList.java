package ex04;

import java.util.UUID;

public interface TransactionsList {
    public void addTransaction(Transaction transaction);
    public void removeById(UUID id);
    public Transaction[] toArray();
}
