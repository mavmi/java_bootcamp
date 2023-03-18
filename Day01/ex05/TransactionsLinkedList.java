package ex05;

import java.util.UUID;

class TransactionsLinkedListNode{
    private TransactionsLinkedListNode previous;
    private TransactionsLinkedListNode next;
    private Transaction transaction;

    TransactionsLinkedListNode(){
        previous = null;
        next = null;
        transaction = null;
    }
    TransactionsLinkedListNode(Transaction transaction){
        previous = null;
        next = null;
        this.transaction = transaction;
    }

    void setPrevious(TransactionsLinkedListNode previous){
        this.previous = previous;
    }
    TransactionsLinkedListNode getPrevious(){
        return previous;
    }

    void setNext(TransactionsLinkedListNode next){
        this.next = next;
    }
    TransactionsLinkedListNode getNext(){
        return next;
    }

    void setTransaction(Transaction transaction){
        this.transaction = transaction;
    }
    Transaction getTransaction(){
        return transaction;
    }
}

public class TransactionsLinkedList implements TransactionsList {
    private TransactionsLinkedListNode first;
    private TransactionsLinkedListNode last;
    private int size;

    public TransactionsLinkedList(){
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        TransactionsLinkedListNode newNode = new TransactionsLinkedListNode(transaction);
        if (size == 0){
            first = newNode;
            last = newNode;
        } else {
            last.setNext(newNode);
            newNode.setPrevious(last);
            last = newNode;
        }
        size++;
    }

    @Override
    public Transaction removeById(UUID id) {
        TransactionsLinkedListNode nodeToDelete = first;
        for (int i = 0; i < size; i++){
            if (nodeToDelete.getTransaction().getIdentifier().equals(id)){
                break;
            }
            nodeToDelete = nodeToDelete.getNext();
        }
        if (nodeToDelete == null){
            throw new TransactionNotFoundException("No transaction with id " + id.toString());
        }
        if (nodeToDelete.getPrevious() != null && nodeToDelete.getNext() != null){
            nodeToDelete.getPrevious().setNext(nodeToDelete.getNext());
            nodeToDelete.getNext().setPrevious(nodeToDelete.getPrevious());
        } else if (nodeToDelete.getPrevious() == null && nodeToDelete.getNext() != null){
            nodeToDelete.getNext().setPrevious(null);
            first = nodeToDelete.getNext();
        } else if (nodeToDelete.getPrevious() != null && nodeToDelete.getNext() == null){
            nodeToDelete.getPrevious().setNext(null);
            last = nodeToDelete.getPrevious();
        } else {
            first = null;
            last = null;
        }
        size--;
        return nodeToDelete.getTransaction();
    }

    @Override
    public Transaction[] toArray() {
        Transaction[] array = new Transaction[size];
        
        TransactionsLinkedListNode node = first;
        for (int i = 0; i < size; i++){
            array[i] = node.getTransaction();
            node = node.getNext();
        }

        return array;
    }
    
}
