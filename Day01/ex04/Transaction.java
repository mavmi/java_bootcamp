package ex04;

import java.util.UUID;

enum TRANSFER_CATEGORY{
    debits,
    credits
}

public class Transaction{
    private UUID Identifier;
    private User Recipient;
    private User Sender;
    private TRANSFER_CATEGORY TransferCategory;
    private Integer TransferAmount;

    public void setIdentifier(UUID Identifier){
        this.Identifier = Identifier;
    }
    public UUID getIdentifier(){
        return Identifier;
    }

    public void setRecipient(User Recipient){
        this.Recipient = Recipient;
    }
    public User getRecipient(){
        return Recipient;
    }

    public void setSender(User Sender){
        this.Sender = Sender;
    }
    public User getSender(){
        return Sender;
    }

    public void setTransferCategory(TRANSFER_CATEGORY TransferCategory){
        this.TransferCategory = TransferCategory;
    }
    public TRANSFER_CATEGORY getTransferCategory(){
        return TransferCategory;
    }

    public void setTransferAmount(Integer TransferAmount){
        if (TransferCategory == TRANSFER_CATEGORY.credits && TransferAmount > 0 ||
                TransferCategory == TRANSFER_CATEGORY.debits && TransferAmount < 0){
            this.TransferAmount = 0;
        } else {
            this.TransferAmount = TransferAmount;
        }
    }
    public Integer getTransferAmount(){
        return TransferAmount;
    }
}
