package ex00;

public class User{
    private Integer Identifier;
    private String Name;
    private Integer Balance;

    public void setIdentifier(Integer Identifier){
        this.Identifier = Identifier;
    }
    public Integer getIdentifier(){
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
