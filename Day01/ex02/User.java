package ex02;

public class User {
    private int Identifier;
    private String Name;
    private Integer Balance;

    public User(){
        this.Identifier = UserIdsGenerator.getInstance().generateId();
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
}
