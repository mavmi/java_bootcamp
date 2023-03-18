package ex04;

public class User {
    private int Identifier;
    private String Name;
    private Integer Balance;
    private TransactionsList transactions;

    public User(){
        this.Identifier = UserIdsGenerator.getInstance().generateId();
        transactions = new TransactionsLinkedList();
    }

    public int getIdentifier(){
        return Identifier;
    }

    public void setName(String Name){
        this.Name = Name;
    }
    public String getName(){
        return Name;
    }

    public void setBalance(Integer Balance){
        if (Balance < 0){
            this.Balance = 0;
        } else {
            this.Balance = Balance;
        }
    }
    public Integer getBalance(){
        return Balance;
    }

    public void setTransactions(TransactionsList transactions){
        this.transactions = transactions;
    }
    public TransactionsList getTransactions(){
        return transactions;
    }
}
