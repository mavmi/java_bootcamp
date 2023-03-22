package edu.school21.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(){

    }
    public EntityNotFoundException(String msg){
        super(msg);
    }
}
